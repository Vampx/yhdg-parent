package cn.com.yusong.yhdg.common.utils;

import okhttp3.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

public class HttpsTest {

    public static void setCertificates(OkHttpClient.Builder builder, InputStream... certificates)
    {
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)
                {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            builder.sslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    public static void main(String[] args) throws Exception {
        InputStream stream = new FileInputStream("C:\\Users\\123\\Desktop\\tomcat.cer");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        setCertificates(builder, stream);
        OkHttpClient client = builder.build();

        String json = "{\n" +
                "\t\"mobile\": \"13777351251\",\n" +
                "\t\"password\": \"asdfasfdasfdasdfasfdasfdasfdasdf\",\n" +
                "\t\"authCode\":\t\"123456\"\n" +
                "}";

        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("https://hdg.yusong.com.cn:8081/api/v1/customer/basic/customer/login.htm").post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
