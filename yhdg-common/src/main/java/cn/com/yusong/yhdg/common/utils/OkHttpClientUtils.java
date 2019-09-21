package cn.com.yusong.yhdg.common.utils;

import cn.com.yusong.yhdg.common.constant.Constant;
import okhttp3.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.com.yusong.yhdg.common.utils.TrustAllCerts.createSSLSocketFactory;

public class OkHttpClientUtils {

    final static Logger log = LogManager.getLogger(OkHttpClientUtils.class);

    public static class HttpResp {
        public int status;
        public String content;

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    final static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .sslSocketFactory(createSSLSocketFactory())
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            })
            .build();




    private static HttpResp uploadFile0(String url, Map<String, File> files, Map<String, String> params, Map<String, String> headers) throws IOException {

        /* form的分割线,自己定义 */
        String boundary = "xx--------------------------------------------------------------xx";
        MultipartBody.Builder builder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);

        for(Map.Entry<String, File> entry : files.entrySet()) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream") , entry.getValue());
            builder.addFormDataPart("file", entry.getKey(), fileBody);
        }

        for(Map.Entry<String, String> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey() , entry.getValue());
        }
        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(Headers.of(headers))
                .build();
        Response response = client.newCall(request).execute();

        HttpResp httpResp = new HttpResp();
        httpResp.status = response.code();
        httpResp.content = response.body().string();
        return httpResp;
    }

    public static HttpResp uploadFile(String url, Map<String, File> files, Map<String, String> param, Map<String, String> header) {
        IOException ex = null;

        for(int i = 0; i < 10; i++) {
            try {
                return uploadFile0(url, files, param, header);
            } catch (IOException e) {
                ex = e;
            }
        }
        throw new RuntimeException(ex);
    }

    private static HttpResp post0(String url, Map<String, String> params) throws IOException {
        if(log.isDebugEnabled()) {
            log.debug("from url: {}", url);
        }

        FormBody.Builder builder = new FormBody.Builder();

        for(Map.Entry<String, String> entry : params.entrySet()) {
            if(log.isDebugEnabled()) {
                log.debug("from param: {}={}", entry.getKey(), entry.getValue());
            }
            builder.add(entry.getKey(), entry.getValue());
        }

        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();

        HttpResp httpResp = new HttpResp();
        httpResp.status = response.code();
        httpResp.content = response.body().string();
        if(log.isDebugEnabled()) {
            log.debug(" recv httpResp.status:{}, ", httpResp.status);
            log.debug(" recv httpResp.content:{}, ", httpResp.content);
        }

        return httpResp;
    }

    public static HttpResp post(String url, Map<String, String> param) {
        IOException ex = null;

        for(int i = 0; i < 3; i++) {
            try {
                return post0(url, param);
            } catch (IOException e) {
                ex = e;
            }
        }
        throw new RuntimeException(ex);
    }


    private static HttpResp post0(String url, String json, Map<String, String> headers) throws IOException {
        if(log.isDebugEnabled()) {
            log.debug("from url: {}", url);
            log.debug("from param: {}", json);
        }

        if(headers == null) {
            headers = Collections.<String, String>emptyMap();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        HttpResp httpResp = new HttpResp();
        httpResp.status = response.code();
        httpResp.content = response.body().string();

        if(log.isDebugEnabled()) {
            log.debug(" recv httpResp.status:{}, ", httpResp.status);
            log.debug(" recv httpResp.content:{}, ", httpResp.content);
        }
        return httpResp;
    }

    public static HttpResp post(String url, String json,  Map<String, String> header) {
        IOException ex = null;

        for(int i = 0; i < 3; i++) {
            try {
                return post0(url, json, header);
            } catch (IOException e) {
                ex = e;
            }
        }
        throw new RuntimeException(ex);
    }

    private static HttpResp get0(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            if(builder.length() != 0) {
                builder.append("&");
            }

            builder.append(entry.getKey());
            builder.append("=");
            builder.append(encodeUrl(entry.getValue()));
        }

        if(url.indexOf("?") >= 0) {
            url += "?" + builder.toString();
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(headers))
                .build();
        Response response = client.newCall(request).execute();

        HttpResp httpResp = new HttpResp();
        httpResp.status = response.code();
        httpResp.content = response.body().string();
        return httpResp;
    }

    public static HttpResp get(String url, Map<String, String> param, Map<String, String> header) {
        IOException ex = null;

        for(int i = 0; i < 10; i++) {
            try {
                return get0(url, param, header);
            } catch (IOException e) {
                ex = e;
            }
        }
        throw new RuntimeException(ex);
    }

    private static String encodeUrl(String source) {
        try {
            return URLEncoder.encode(source, Constant.ENCODING_UTF_8);
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String postJson(OkHttpClient client, String url, String json, Headers headers) {
        if(headers == null) {
            headers = Headers.of(Collections.<String, String>emptyMap());
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(url)
                .headers(headers)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }
}
