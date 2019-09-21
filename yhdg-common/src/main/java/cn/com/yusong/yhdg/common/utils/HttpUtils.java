package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HttpUtils {
    public static class HttpResp {
        public int status;
        public String content;
    }

    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1000 * 2)
            .setConnectTimeout(1000 * 2)
            .build();

    private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    static {
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(200);
    }
    private static CloseableHttpClient httpclient = HttpClients.custom()
            .setConnectionManager(cm)
            .setDefaultRequestConfig(requestConfig)
            .build();

    public static HttpResp uploadFile(String url, File file, Map<String, String> param) throws IOException {
        HttpResp httpResp = new HttpResp();

        HttpPost httppost = new HttpPost(url);

        FileBody fileBody = new FileBody(file);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Constant.CHARSET_UTF_8);
        builder.addPart("file", fileBody);

        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        for(Map.Entry<String, String> entry : param.entrySet()) {
            builder.addPart(entry.getKey(), new StringBody(entry.getValue(), contentType));
        }
        HttpEntity httpEntity = builder.build();

        httppost.setEntity(httpEntity);

        CloseableHttpResponse response = httpclient.execute(httppost);
        httpResp.status = response.getStatusLine().getStatusCode();

        try {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                httpResp.content = EntityUtils.toString(responseEntity, "UTF-8");
            }
            EntityUtils.consume(responseEntity);
        } finally {
            response.close();
        }

        return httpResp;
    }
}
