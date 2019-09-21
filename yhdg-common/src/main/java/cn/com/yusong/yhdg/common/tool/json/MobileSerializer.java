package cn.com.yusong.yhdg.common.tool.json;

import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class MobileSerializer extends JsonSerializer<String> {

    public static boolean MASK = true;

    @Override
    public void serialize(String o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(o == null) {
            jsonGenerator.writeNull();
        } else {
            if (MASK && o != null && o.length() >= 11) {
                jsonGenerator.writeRawValue(AppUtils.getMobileMask(o));
            } else {
                jsonGenerator.writeRawValue(o);
            }
        }
    }
}
