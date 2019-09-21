package cn.com.yusong.yhdg.staticserver.tools.weixin;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class WxPayUtils {
    static final Logger log = LogManager.getLogger(WxPayUtils.class);

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
