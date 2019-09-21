package cn.com.yusong.yhdg.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;

/**
 * 压缩包工具类
 */
public class ZipUtils {
    public static void unzip(File zip, File outputDir)  {

        try {
            byte[] bufferByte = new byte[1024];

            ZipFile zipFile = new ZipFile(zip, "GBK");
            Enumeration e = zipFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String name = entry.getName();
                File file = new File(outputDir, name);   // file.getParent() 输出目录
                if (entry.isDirectory()) {   // 是目录
                    file.mkdir();
                } else {
                    File parent = file.getParentFile();   // 判断文件父目录是否存在
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    FileOutputStream outputStream = new FileOutputStream(file);
                    InputStream inputStream = zipFile.getInputStream(entry);
                    int bufferSize;
                    while ((bufferSize = inputStream.read(bufferByte)) > 0) {  // 缓冲流
                        outputStream.write(bufferByte, 0, bufferSize);
                    }
                    inputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zip(File targetFile, Map<File, String> map) throws IOException {
        ZipOutputStream zipOutputStream = null;
        byte[] bufferByte = new byte[1024 * 10];

        try {
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)));
            zipOutputStream.setEncoding("GBK");

            for(Map.Entry<File, String> entry : map.entrySet()) {
                File file = entry.getKey();
                String path = entry.getValue();

                FileInputStream fileStream = new FileInputStream(file);
                ZipEntry zip = new ZipEntry(path);  // 构造实体
                zipOutputStream.putNextEntry(zip);

                int bufferSize;
                while ((bufferSize = fileStream.read(bufferByte)) > 0) {  // 读字节流缓冲
                    zipOutputStream.write(bufferByte, 0, bufferSize);
                }
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            }


        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
    }
}
