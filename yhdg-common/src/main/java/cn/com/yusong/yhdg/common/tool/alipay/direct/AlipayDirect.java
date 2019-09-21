package cn.com.yusong.yhdg.common.tool.alipay.direct;

import cn.com.yusong.yhdg.common.tool.alipay.AlipayUtils;
import cn.com.yusong.yhdg.common.tool.alipay.DSA;
import cn.com.yusong.yhdg.common.tool.alipay.MD5;
import cn.com.yusong.yhdg.common.tool.alipay.RSA;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * alipaydirect即时到账交易接口
 */
public class AlipayDirect {

    private static final String ENCODING = "UTF-8";
    private static final String ALIPAY_GATEWAY_SERVICE = "https://mapi.alipay.com/gateway.do?";

    public static class Order {
        public String outTradeNo; //商户网站订单系统中唯一订单号，必填
        public String subject; //订单名称
        public String totalFee; //付款金额
        public String body; //订单描述

        public String partner; // 合作身份者ID，以2088开头由16位纯数字组成的字符串
        public String sellerEmail; // 收款支付宝账号
        public String key; // 商户的私钥

        public String buyerEmail; //卖家支付宝账号

        public String paymentType = "1";
        public String charset = "UTF-8";
        public String signType = "MD5";

        public String notifyUrl;
        public String returnUrl;

        public String extraCommonParam = ""; //公用回传参数
        public String itbpay = ""; //超时时间

        public String toParam() {
            Map<String, String> param = new HashMap<String, String>();
            param.put("service", "create_direct_pay_by_user");
            param.put("partner", partner);
            param.put("seller_email", sellerEmail);

            if(StringUtils.isNotEmpty(buyerEmail)) {
                param.put("buyer_email", buyerEmail);
            }

            param.put("_input_charset", charset);
            param.put("payment_type", paymentType);
            param.put("notify_url", notifyUrl);

            if(StringUtils.isNotEmpty(returnUrl)) {
                param.put("return_url", returnUrl);
            }

            param.put("out_trade_no", outTradeNo);
            param.put("subject", subject);
            param.put("total_fee", totalFee);
            if(StringUtils.isNotEmpty(body)) {
                param.put("body", body);
            }
            if(StringUtils.isNotEmpty(extraCommonParam)) {
                param.put("extra_common_param", extraCommonParam);
            }
            if(StringUtils.isNotEmpty(itbpay)) {
                param.put("it_b_pay", itbpay);
            }

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

            param.put("sign_type", signType);

            if(signType.equals("MD5")) {
                param.put("sign", MD5.sign(builder.toString(), key, charset));
            } else if(signType.equals("RSA")) {
                param.put("sign", RSA.sign(builder.toString(), key, charset));
            } else if(signType.equals("DSA")) {
                param.put("sign", DSA.sign(builder.toString(), key, charset));
            }



            List<String> list = new ArrayList<String>(param.size());
            for(String key : param.keySet()) {
                String value = param.get(key);
                list.add(String.format("%s=%s", key, encoding(value)));
            }
            return StringUtils.join(list, "&");
        }

        private String encoding(String value) {
            try {
                return URLEncoder.encode(value, ENCODING);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Order order = new Order();
        order.outTradeNo = "SE2015080300000ccc234dccc000000015";
        order.subject = "寄件订单";
        order.totalFee = "0.01";
        order.body = "寄件订单";

        order.partner = "2088021161956728";
        order.sellerEmail = "ys@yusong.com.cn";
        order.key = "211svfxl2t1zkmoyk4qcie91nvenb341";

        order.signType = "DSA";
        order.key = DSA.readPrivateKey();

        order.signType = "MD5";
        order.key = AlipayUtils.SELLER_KEY;

        order.notifyUrl = "http://115.236.183.238:8090/security/basic/send_order/alipay_pay_ok.htm";

        System.out.println(ALIPAY_GATEWAY_SERVICE + order.toParam());
    }
}
