package cn.com.yusong.yhdg.common.tool.weixin;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

/**
 * 微信支付参数
 */
public class WxPayParam {

    protected Map<String, String> attributes = new HashMap<String, String>();

    public String sign(String signKey) {
        List<String> keys = new ArrayList<String>(attributes.keySet());
        Collections.sort(keys);

        StringBuilder toSign = new StringBuilder();
        for (String key : keys) {
            String value = attributes.get(key);
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
