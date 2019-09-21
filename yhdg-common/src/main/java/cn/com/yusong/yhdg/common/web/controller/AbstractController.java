package cn.com.yusong.yhdg.common.web.controller;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.spring.DateEditor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class AbstractController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    protected void downloadSupport(File file, HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {

        String agent = request.getHeader("User-Agent");
        if (agent != null) {
            agent = agent.toLowerCase();
            if (agent.indexOf("firefox") != -1) {
                fileName = new String(fileName.getBytes(),"ISO-8859-1");
            } else if (agent.indexOf("msie") != -1) {
                fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
            } else {
                fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
            }
        }

        response.setHeader("Content-disposition", String.format("attachment;filename=%s", fileName));
        response.setContentType("application/octet-stream");
        response.setBufferSize(1024 * 10);

        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            byte[] buf = new byte[1024 * 10];
            int count = -1;
            while( (count = inputStream.read(buf)) != -1) {
                response.getOutputStream().write(buf, 0, count);
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    public String formatPath(String firstPath, String fileName , String fileSuffix) {
        return String.format("%s%s/%s.%s", firstPath, DateFormatUtils.format(new Date(), Constant.DATE_FORMAT),fileName, fileSuffix);
    }

    public String formatPath(String firstPath, int id, String fileName , String fileSuffix) {
        return String.format("%s%d%s/%s.%s", firstPath, id,DateFormatUtils.format(new Date(), Constant.DATE_FORMAT),fileName, fileSuffix);
    }
}
