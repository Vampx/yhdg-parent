package cn.com.yusong.yhdg.common.tool.weixin;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

/**
 * 微信支付结果
 */
public abstract class WxPayResult {
    protected String xml;
    protected Map<String, String> attributes = new HashMap<String, String>();

    public WxPayResult(String xml) throws DocumentException {
        this.xml = xml;
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        List<Element> elementList = rootElement.elements();
        if(elementList != null) {
            for(Element element : elementList) {
                attributes.put(element.getQualifiedName(), element.getTextTrim());
            }
        }
        read();
    }

    public String getXml() {
        return xml;
    }

    protected abstract void read();
    protected abstract String getSign();

    public boolean signOk(String signKey) {
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
        return sign.equals(getSign());
    }

    public static void main(String[] args) {
        System.out.println(CodecUtils.md5( 0 + "|" +"http://localhost:8080www" + "|" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK"));
    }
}
