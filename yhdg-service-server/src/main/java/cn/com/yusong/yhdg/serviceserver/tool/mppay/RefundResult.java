package cn.com.yusong.yhdg.serviceserver.tool.mppay;

import cn.com.yusong.yhdg.common.tool.weixin.WxPayResult;
import org.dom4j.DocumentException;

/**
 * 企业付款结果
 */
public class RefundResult extends WxPayResult {

    public String return_code;
    public String return_msg;
    public String mch_appid;
    public String mchid;
    public String device_info;
    public String nonce_str;
    public String result_code;
    public String err_code;
    public String err_code_des;
    public String partner_trade_no;
    public String payment_no;
    public String payment_time;

    public RefundResult(String xml) throws DocumentException {
        super(xml);
    }

    @Override
    protected void read() {
        return_code = attributes.get("return_code");
        return_msg = attributes.get("return_msg");
        mch_appid = attributes.get("mch_appid");
        mchid = attributes.get("mchid");
        device_info = attributes.get("device_info");
        nonce_str = attributes.get("nonce_str");
        result_code = attributes.get("result_code");
        err_code = attributes.get("err_code");
        err_code_des = attributes.get("err_code_des");
        partner_trade_no = attributes.get("partner_trade_no");
        payment_no = attributes.get("payment_no");
        payment_time = attributes.get("payment_time");
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(result_code);
    }

    @Override
    protected String getSign() {
        throw new UnsupportedOperationException();
    }
}
