package cn.com.yusong.yhdg.serviceserver.tool.mppay;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * 微信企业付款
 */
public class MpPayUtils {
    final static Logger log = LogManager.getLogger(MpPayUtils.class);

    public static class HttpResult {
        public StatusLine statusLine;
        public String content;

        public HttpResult(StatusLine statusLine, String content) {
            this.statusLine = statusLine;
            this.content = content;
        }
    }

    public final static HttpResult send(File keyFile, String partnerId, String xml) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        InputStream instream = new FileInputStream(keyFile);
        try {
            keyStore.load(instream, partnerId.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, partnerId.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            log.info("send xml: {}", xml);


            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
            StringEntity se = new StringEntity(xml, "UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = IOUtils.toString(entity.getContent());
                    log.info("resp xml: {}", result);
                    return new HttpResult(response.getStatusLine(), result);
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

        return null;
    }

    /**
     <?xml version="1.0" encoding="UTF-8"?>
     <xml>
     <mch_appid>wxa4b30215e5213056</mch_appid>
     <mchid>1483183652</mchid>
     <nonce_str>1501408758769</nonce_str>
     <partner_trade_no>ZZ00000001</partner_trade_no>
     <openid>oLo870iNn9RxFbPVl9FvM5BpDDqo</openid>
     <check_name>FORCE_CHECK</check_name>
     <re_user_name>肖劼</re_user_name>
     <amount>100</amount>
     <desc>测试</desc>
     <spbill_create_ip>127.0.0.1</spbill_create_ip>
     <sign>3BBBD681F5D84895CBAD938D5CF786C3</sign>
     </xml>
     log4j:WARN No appenders could be found for logger (org.apache.http.client.protocol.RequestAddCookies).
     log4j:WARN Please initialize the log4j system properly.
     200
     <xml>
     <return_code><![CDATA[SUCCESS]]></return_code>
     <return_msg><![CDATA[]]></return_msg>
     <nonce_str><![CDATA[1501408758769]]></nonce_str>
     <result_code><![CDATA[SUCCESS]]></result_code>
     <partner_trade_no><![CDATA[ZZ00000001]]></partner_trade_no>
     <payment_no><![CDATA[1000018301201707308768674817]]></payment_no>
     <payment_time><![CDATA[2017-07-30 17:59:32]]></payment_time>
     </xml>
     */

    public static void main(String[] args) throws Exception {
        RefundParam param = new RefundParam();
        param.mch_appid = "wxa4b30215e5213056";
        param.mchid = "1483183652";
        param.nonce_str = String.format("%d", System.currentTimeMillis());
        param.partner_trade_no = "ZZ00000001";
        param.openid = "oLo870iNn9RxFbPVl9FvM5BpDDqo";
        param.check_name= "FORCE_CHECK";
        param.re_user_name = "肖劼";
        param.amount = "100";
        param.desc = "测试";
        param.spbill_create_ip = "127.0.0.1";

        String xml = param.toXml("werty1234567QWERTYUZXCVBNMQWERTY");
        System.out.println(xml);

        HttpResult result = send(new File("D:\\work\\充电桩\\证书\\斑马充\\apiclient_cert.p12"), "", xml);
        System.out.println(result.statusLine.getStatusCode());
        System.out.println(result.content);
    }
}
