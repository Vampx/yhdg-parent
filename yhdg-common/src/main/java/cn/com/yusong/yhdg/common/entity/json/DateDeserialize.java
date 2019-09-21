package cn.com.yusong.yhdg.common.entity.json;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class DateDeserialize extends JsonDeserializer<Date> {
  

    @Override  
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        try {
            if(jp.getText().length() > 10) {
                return DateUtils.parseDate(jp.getText(), new String[]{Constant.DATE_TIME_FORMAT});
            } else {
                return DateUtils.parseDate(jp.getText(), new String[]{Constant.DATE_FORMAT});
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}