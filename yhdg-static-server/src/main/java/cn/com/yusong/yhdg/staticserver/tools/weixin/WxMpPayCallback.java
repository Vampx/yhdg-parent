package cn.com.yusong.yhdg.staticserver.tools.weixin;

import cn.com.yusong.yhdg.common.tool.weixin.WxPayResult;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.DocumentException;

/**
 * pre> 订单支付状态回调
 *
 * 支付结果通知(详见http://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7)
 *
 * /pre>
 *
 * @author ukid
 */
public class WxMpPayCallback extends WxPayResult {

    public String return_code;
    public String return_msg;

    public String appid;
    public String mch_id;
    public String device_info;
    public String nonce_str;
    public String sign;
    public String result_code;
    public String err_code;
    public String err_code_des;
    public String openid;
    public String is_subscribe;
    public String trade_type;
    public String bank_type;
    public String total_fee;
    public String fee_type;
    public String cash_fee;
    public String cash_fee_type;
    public String coupon_fee;
    public String coupon_count;
    public String transaction_id;
    public String out_trade_no;
    public String attach;
    public String time_end;

    public WxMpPayCallback(String xml) throws DocumentException {
        super(xml);
    }

    @Override
    protected void read() {
        return_code = attributes.get("return_code");
        return_msg = attributes.get("return_msg");

        appid = attributes.get("appid");
        mch_id = attributes.get("mch_id");
        device_info = attributes.get("device_info");
        nonce_str = attributes.get("nonce_str");
        sign = attributes.get("sign");
        result_code = attributes.get("result_code");
        err_code = attributes.get("err_code");
        err_code_des = attributes.get("err_code_des");
        openid = attributes.get("openid");
        is_subscribe = attributes.get("is_subscribe");
        trade_type = attributes.get("trade_type");
        bank_type = attributes.get("bank_type");
        total_fee = attributes.get("total_fee");
        fee_type = attributes.get("fee_type");
        cash_fee = attributes.get("cash_fee");
        cash_fee_type = attributes.get("cash_fee_type");
        coupon_fee = attributes.get("coupon_fee");
        coupon_count = attributes.get("coupon_count");
        transaction_id = attributes.get("transaction_id");
        out_trade_no = attributes.get("out_trade_no");
        attach = attributes.get("attach");
        time_end = attributes.get("time_end");
    }

    @Override
    protected String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this.attributes, ToStringStyle.MULTI_LINE_STYLE);
    }
}