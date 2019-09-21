package cn.com.yusong.yhdg.common.entity.json;

import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateTimeNotSecondSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(DateFormatUtils.format(o, Constant.DATE_TIME_NOT_SECOND_FORMAT));
    }
}
