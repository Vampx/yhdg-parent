package cn.com.yusong.yhdg.common.tool.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class DoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(o == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeRawValue(String.format("%d", o.intValue()));
        }
    }
}
