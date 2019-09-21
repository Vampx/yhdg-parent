package cn.com.yusong.yhdg.common.test;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiTestHelper extends TestHelper {

    Map<String, Object> attributes = new HashMap<String, Object>();

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    public Long getLongAttribute(String key) {
        return (Long) attributes.get(key);
    }
    public Integer getIntAttribute(String key) {
        return (Integer) attributes.get(key);
    }
    public Short getShortAttribute(String key) {
        return (Short) attributes.get(key);
    }
    public Float getFloatAttribute(String key) {
        return (Float) attributes.get(key);
    }
    public Double getDoubleAttribute(String key) {
        return (Double) attributes.get(key);
    }
    public String getStringAttribute(String key) {
        return (String) attributes.get(key);
    }

    public void initClient(OkHttpClient.Builder builder) {
    }

    public OkHttpClient buildHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        initClient(builder);
        return builder.build();
    }

    public OkHttpClient buildHttpsClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        initClient(builder);

        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };

            SSLContext e = SSLContext.getInstance("TLS");

            e.init(null, new TrustManager[]{tm}, null);

            builder.sslSocketFactory(e.getSocketFactory());

            builder.hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

    public String postJson(OkHttpClient client, String url, String json) {
        return postJson(client, url, json, null);
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

    public Object getValue(Map map, String property) {
        OgnlContext context = new OgnlContext();
        context.put("map", map);

        try {
            Object obj = Ognl.getValue("#map." + property, context, context.getRoot());
            return obj;
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getLongValue(Map map, String property) {
        return (Long) getValue(map, property);
    }
    public Integer getIntValue(Map map, String property) {
        return (Integer) getValue(map, property);
    }
    public Short getShortValue(Map map, String property) {
        return (Short) getValue(map, property);
    }
    public Float getFloatValue(Map map, String property) {
        return (Float) getValue(map, property);
    }
    public Double getDoubleValue(Map map, String property) {
        return (Double) getValue(map, property);
    }
    public String getStringValue(Map map, String property) {
        return (String) getValue(map, property);
    }

}
