package cn.com.yusong.yhdg.agentserver.weixin;


import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.util.*;

public class WxPayUtils {
    static final Logger log = LogManager.getLogger(WxPayUtils.class);

    public static WxPayUnifiedOrderResult refundOrder(
            String appId,
            String mchId,
            String partnerKey,
            String outTradeNo,
            int totalFee,
            int refundFee,
            String refundDesc,
            File file) throws Exception {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("appid", appId);
        parameters.put("mch_id", mchId);
        parameters.put("out_trade_no", outTradeNo);
        parameters.put("out_refund_no", System.currentTimeMillis() + "");
        parameters.put("total_fee", String.format("%d", totalFee));
        parameters.put("refund_fee", String.format("%d", refundFee));
        parameters.put("refund_desc", refundDesc);
        parameters.put("nonce_str", System.currentTimeMillis() + "");

        final SortedMap<String, String> packageParams = new TreeMap<String, String>(parameters);
        String sign = createSign(packageParams, partnerKey);
        packageParams.put("sign", sign);

        StringBuilder request = new StringBuilder("<xml>");
        for (Map.Entry<String, String> para : packageParams.entrySet()) {
            request.append(String.format("<%s>%s</%s>", para.getKey(),
                    para.getValue(), para.getKey()));
        }

        request.append("</xml>");

        if (log.isDebugEnabled()) {
            log.debug("send xml: {}", request.toString());
        }

        String xml = WxHttpsUtils.send("https://api.mch.weixin.qq.com/secapi/pay/refund", request.toString(), mchId, file);
        if (StringUtils.isNotEmpty(xml)) {
            if (log.isDebugEnabled()) {
                log.debug("recv xml: {}", xml);
            }

            WxPayUnifiedOrderResult result = new WxPayUnifiedOrderResult();

            Document document = DocumentHelper.parseText(xml);
            Element element = (Element) document.selectSingleNode("/xml/return_code");
            if (element != null) {
                result.setReturnCode(element.getTextTrim());
            }

            element = (Element) document.selectSingleNode("/xml/return_msg");
            if (element != null) {
                result.setReturnMsg(element.getTextTrim());
            }

            element = (Element) document.selectSingleNode("/xml/err_code");
            if (element != null) {
                result.setErrCode(element.getTextTrim());
            }

            element = (Element) document.selectSingleNode("/xml/err_code_des");
            if (element != null) {
                result.setErrCodeDes(element.getTextTrim());
            }

            element = (Element) document.selectSingleNode("/xml/result_code");
            if (element != null) {
                result.setResultCode(element.getTextTrim());
            }

            return result;
        }

        return null;
    }

    public static String createSign(Map<String, String> packageParams,
                                    String signKey) {
        SortedMap<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.putAll(packageParams);

        List<String> keys = new ArrayList<String>(packageParams.keySet());
        Collections.sort(keys);

        StringBuffer toSign = new StringBuffer();
        for (String key : keys) {
            String value = packageParams.get(key);
            if (null != value && !"".equals(value) && !"sign".equals(key)
                    && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        toSign.append("key=" + signKey);
        String sign = DigestUtils.md5Hex(toSign.toString()).toUpperCase();
        return sign;
    }
}
