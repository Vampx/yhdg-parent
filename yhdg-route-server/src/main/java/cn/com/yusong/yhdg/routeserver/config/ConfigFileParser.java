package cn.com.yusong.yhdg.routeserver.config;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ConfigFileParser extends DefaultPropertiesPersister {
    @Override
    public void loadFromXml(Properties props, InputStream is) throws IOException {
        String xml = IOUtils.toString(is, Constant.ENCODING_UTF_8);
        try {
            Document document = DocumentHelper.parseText(xml);
            List<Element> list = document.selectNodes("/Config/Entry");

            for(Element element : list) {
                props.put(element.attributeValue("key"), element.getTextTrim());
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
