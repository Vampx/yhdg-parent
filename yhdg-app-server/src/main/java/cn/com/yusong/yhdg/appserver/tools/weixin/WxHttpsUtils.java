package cn.com.yusong.yhdg.appserver.tools.weixin;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class WxHttpsUtils {
    static final Logger log = LogManager.getLogger(WxHttpsUtils.class);
    static final int HTTP_RETRY = 10;

    public static String send(String resource, String content) throws Exception {
        return send0(resource, content);
    }

    protected static String send0(String resource, String content) throws Exception {
        String response;
        for(int i = 0; i < HTTP_RETRY; i++) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(resource);
            HttpsURLConnection conn = null;

            try {
                conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                if(StringUtils.isNotEmpty(content)) {
                    conn.getOutputStream().write(content.getBytes(Charset.forName("UTF-8")));
                    conn.getOutputStream().flush();
                }

                if(conn.getResponseCode() != 200) {
                    throw new IllegalStateException();
                }

                response = IOUtils.toString(conn.getInputStream(), "UTF-8");
               return response;
            } catch (Exception ex) {
                log.error(ex);
            } finally {
                consumeInputStream(conn);
                consumeErrorStream(conn);
            }
        }
        return null;
    }

    private static void consumeInputStream(HttpsURLConnection conn) {
        if(conn != null) {
            try {
                int respCode = ((HttpsURLConnection)conn).getResponseCode();
                InputStream es = ((HttpsURLConnection)conn).getInputStream();
                if(es != null) {
                    int ret = 0;
                    byte[] buf = new byte[1024];
                    while ((ret = es.read(buf)) > 0) {
                    }
                    es.close();
                }

            } catch(IOException ex) {
            }
        }
    }

    private static void consumeErrorStream(HttpsURLConnection conn) {
        if(conn != null) {
            try {
                int respCode = ((HttpsURLConnection)conn).getResponseCode();
                InputStream es = ((HttpsURLConnection)conn).getErrorStream();
                if(es != null) {
                    int ret = 0;
                    byte[] buf = new byte[1024];
                    while ((ret = es.read(buf)) > 0) {
                    }
                    es.close();
                }

            } catch(IOException ex) {
            }
        }
    }
}
