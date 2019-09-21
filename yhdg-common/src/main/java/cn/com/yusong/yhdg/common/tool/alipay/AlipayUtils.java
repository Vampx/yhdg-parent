package cn.com.yusong.yhdg.common.tool.alipay;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class AlipayUtils {
    public static final String SELLER_PARTNER = "2088821834281814";
    public static final String SELLER_EMAIL = "public@exiu99.com";
    public static final String SELLER_KEY = "211svfxl2t1zkmoyk4qcie91nvenb341";

    public static final String TRADE_STATUS_FINISHED = "TRADE_FINISHED";
    public static final String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";
    public static final String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_DSA = "DSA";
    public static final String ALGORITHM_RSA2 = "RSA2";

    public static final String ALIPAY_GATEWAY_SERVICE = "https://mapi.alipay.com/gateway.do";

    public static final String NOTIFY_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&partner=%s&notify_id=%s";

    public static void main1(String[] args) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("pay_date", "20150811");
        map.put("batch_no", "2");
        map.put("_input_charset", "utf-8");
        map.put("notify_url", "http://115.236.183.238:8090/security/basic/withdrawal/alipay_pay_ok.htm");
        map.put("batch_fee", "0.01");
        map.put("batch_num", "1");
        map.put("partner", "2088021161956728");
        map.put("service", "batch_trans_notify");
        map.put("account_name", "肖相槐");
        map.put("email", "ys@yusong.com.cn");
        map.put("detail_data", "1^13777351251^徐晓蕾^0.01^提现");

        sign(map, "MD5", SELLER_KEY, "UTF-8");
        System.out.println(map.get("sign"));
    }

    public static void main(String[] args) {
        Map<String, String> param = new HashMap<String, String>();

        param.put("discount", "0.00");
        param.put("payment_type", "1");
        param.put("subject", "快递柜用户充值");
        param.put("trade_no", "2015092500001000000064497533");
        param.put("buyer_email", "15968862022");
        param.put("gmt_create", "2015-09-25 08:58:04");
        param.put("notify_type", "trade_status_sync");
        param.put("quantity", "1");
        param.put("out_trade_no", "AADD2015092500000000000000000007");
        param.put("seller_id", "2088021161956728");
        param.put("notify_time", "2015-09-25 09:12:19");
        param.put("body", "快递柜上寄件，派件使用的用户充值");
        param.put("trade_status", "TRADE_SUCCESS");
        param.put("is_total_fee_adjust", "N");
        param.put("total_fee", "0.01");
        param.put("gmt_payment", "2015-09-25 08:58:05");
        param.put("seller_email", "ys@yusong.com.cn");
        param.put("price", "0.01");
        param.put("buyer_id", "2088702156816000");
        param.put("notify_id", "445f0b150d434cb3ac60b5bef73b7eb020");
        param.put("use_coupon", "N");

        String sign = "Xk7bDON2IpiRp3/dEThYIRX7s9SNKtiAZiqOy8E2DPvOGLBYgH2pksW5Uk6AUvnQnTF3E3xnI2GzEXKw0dl6F2lSlj9WifuuQGmRCzOE0wg7UetxVk6BV5f8OFkf4WfWbe5NsmaIfMNfeLDsUdGX6srsdD9hM7PZBsnEm4+reXk=";
        System.out.println(signVerify(param, "RSA", sign, RSA.readAlipayKey(), "UTF-8"));
    }

    public static void main2(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("discount", "0.00");
//        map.put("payment_type", "1");
//        map.put("subject", "寄件订单");
//        map.put("trade_no", "2015080300001000010059199491");
//        map.put("buyer_email", "13777351251");
//        map.put("gmt_create", "2015-08-03 12:48:12");
//        map.put("notify_type", "trade_status_sync");
//        map.put("quantity", "1");
//        map.put("out_trade_no", "SE2015080300000000000000000002");
//        map.put("notify_time", "2015-08-03 12:48:46");
//        map.put("seller_id", "2088021161956728");
//        map.put("body", "寄件订单");
//        map.put("trade_status", "TRADE_SUCCESS");
//        map.put("is_total_fee_adjust", "N");
//        map.put("total_fee", "0.01");
//        map.put("gmt_payment", "2015-08-03 12:48:46");
//        map.put("seller_email", "ys@yusong.com.cn");
//        map.put("price", "0.01");
//        map.put("buyer_id", "2088902534968013");
//        map.put("notify_id", "ab4bd4d962431994543b084bf07b87d822");
//        map.put("price", "0.01");
//        map.put("use_coupon", "N");

        map.put("discount", "0.00");
        map.put("payment_type", "1");
        map.put("subject", "寄件订单");
        map.put("trade_no", "2015080300001000010059210103");
        map.put("buyer_email", "13777351251");
        map.put("gmt_create", "2015-08-03 15:36:24");
        map.put("notify_type", "trade_status_sync");
        map.put("quantity", "1");
        map.put("out_trade_no", "SE2015080300000000000000000003");
        map.put("notify_time", "2015-08-03 15:36:39");
        map.put("seller_id", "2088021161956728");
        map.put("body", "寄件订单");
        map.put("trade_status", "TRADE_SUCCESS");
        map.put("is_total_fee_adjust", "N");
        map.put("total_fee", "0.01");
        map.put("gmt_payment", "2015-08-03 15:36:39");
        map.put("seller_email", "ys@yusong.com.cn");
        map.put("price", "0.01");
        map.put("buyer_id", "2088902534968013");
        map.put("notify_id", "8867aef4c090aefd4cf693c45d84031622");
        map.put("use_coupon", "N");

        System.out.println(signVerify(map, "MD5", "1810103e4a8c07440f6d60771181e9d1", AlipayUtils.SELLER_KEY, "UTF-8"));;
    }

    public static boolean signVerify(Map<String, String> param, String signType, String sign,  String secretKey, String charset) {
        StringBuilder builder = new StringBuilder();
        List<String> keys = new ArrayList<String>(param.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = param.get(key);

            builder.append(key);
            builder.append('=');
            builder.append(value);
            if(i != keys.size() - 1) {
                builder.append("&");
            }
        }

        if(signType.equals(ALGORITHM_MD5)) {
            return MD5.verify(builder.toString(), sign, secretKey, charset);
        } else if(signType.equals(ALGORITHM_RSA)) {
            return RSA.verify(builder.toString(), sign, secretKey, charset);
        } else if(signType.equals(ALGORITHM_DSA)) {
            return DSA.verify(builder.toString(), sign, secretKey, charset);
        } else if(signType.equals(ALGORITHM_RSA2)) {
            return RSA2.verify(builder.toString(), sign, secretKey, charset);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void sign(Map<String, String> param, String signType, String secretKey, String charset) {
        StringBuilder builder = new StringBuilder();
        List<String> keys = new ArrayList<String>(param.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = param.get(key);

            builder.append(key);
            builder.append('=');
            builder.append(value);
            if(i != keys.size() - 1) {
                builder.append("&");
            }
        }

        if(signType.equals("MD5")) {
            param.put("sign", MD5.sign(builder.toString(), secretKey, charset));
        } else if(signType.equals("RSA")) {
            param.put("sign", RSA.sign(builder.toString(), secretKey, charset));
        } else if(signType.equals("DSA")) {
            param.put("sign", DSA.sign(builder.toString(), secretKey, charset));
        }
        param.put("sign_type", signType);
    }

    public static String accessHttps(String resourceUrl, String charset) throws Exception {

        HttpClient httpclient = new DefaultHttpClient();
        //Secure Protocol implementation.
        SSLContext ctx = SSLContext.getInstance("SSL");
        //Implementation of a trust manager for X509 certificates
        X509TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
        ctx.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);

        ClientConnectionManager ccm = httpclient.getConnectionManager();
        String result = "";
        try {
            //register https protocol in httpclient's scheme registry
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            HttpGet httpget = new HttpGet(resourceUrl);
            HttpResponse response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            result = new String(IOUtils.toByteArray(entity.getContent()), charset);
            EntityUtils.consume(entity); // Consume response content
        } finally {
            ccm.shutdown();
        }
        return result;
    }

    public static void encode(Map<String, String> param, String charset) {
        for(String key : param.keySet()) {
            String value = param.get(key);
            param.put(key, encode(value, charset));
        }
    }

    public static String encode(String value, String charset) {
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
