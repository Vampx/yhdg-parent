package cn.com.yusong.yhdg.serviceserver.tool.mppay;

import cn.com.yusong.yhdg.common.tool.weixin.WxPayParam;

/**
 * 企业付款参数
 */
public class RefundParam extends WxPayParam {

    public String mch_appid;
    public String mchid;
    public String nonce_str;
    public String partner_trade_no;
    public String openid;
    public String check_name= "FORCE_CHECK";
    public String re_user_name;
    public String amount;
    public String desc;
    public String spbill_create_ip;

    public String toXml(String partnerKey) {
        attributes.put("mch_appid", mch_appid);
        attributes.put("mchid", mchid);
        attributes.put("nonce_str", nonce_str);
        attributes.put("partner_trade_no", partner_trade_no);
        attributes.put("openid", openid);
        attributes.put("check_name", check_name);
        attributes.put("re_user_name", re_user_name);
        attributes.put("amount", amount);
        attributes.put("desc", desc);
        attributes.put("spbill_create_ip", spbill_create_ip);
        String sign = sign(partnerKey);

        String xml = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xml>\n" +
                "<mch_appid>%s</mch_appid>\n" +
                "<mchid>%s</mchid>\n" +
                "<nonce_str>%s</nonce_str>\n" +
                "<partner_trade_no>%s</partner_trade_no>\n" +
                "<openid>%s</openid>\n" +
                "<check_name>%s</check_name>\n" +
                "<re_user_name>%s</re_user_name>\n" +
                "<amount>%s</amount>\n" +
                "<desc>%s</desc>\n" +
                "<spbill_create_ip>%s</spbill_create_ip>\n" +
                "<sign>%s</sign>\n" +
                "</xml>", mch_appid, mchid, nonce_str, partner_trade_no, openid, check_name, re_user_name, amount, desc, spbill_create_ip, sign);
        return xml;
    }
}
