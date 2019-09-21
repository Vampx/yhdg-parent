package cn.com.yusong.yhdg.appserver.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadFileUtils {

    private final static Logger log = LogManager.getLogger(DownloadFileUtils.class);

    private static final OkHttpClient client = new OkHttpClient();

    public static void download(String url, File file) {
        for(int i = 0; i < 10; i++) {
            try {
                download0(url, file);
                break;
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private static void download0(String url, File file) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        FileOutputStream fos = null;
        try {
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            Response response = client.newCall(request).execute();
            IOUtils.copy(response.body().byteStream(), fos);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
