package cn.com.yusong.yhdg.agentserver.entity.result;

import cn.com.yusong.yhdg.agentserver.constant.RespCode;
import cn.com.yusong.yhdg.agentserver.entity.exception.RespException;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.OutputStream;

public class RestResult {
    public static final RestResult SUCCESS = new RestResult();

    int code;
    String message;
    Object data;

    public RestResult() {
    }

    public RestResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RestResult result(RespCode respCode) {
        RestResult result = new RestResult(respCode.getValue(), respCode.getName());
        return result;
    }

    public static RestResult result(int code, String message) {
        RestResult result = new RestResult(code, message);
        return result;
    }
    public static RestResult result(RespException exception){
        return new RestResult(exception.getErrorCode(), exception.getErrorMsg());
    }

    public static DataResult dataResult(int code, String message, Object data) {
        DataResult result = new DataResult(code, message, data);
        return result;
    }

    public static MapResult mapResult(int code, String message) {
        MapResult result = new MapResult(code, message);
        return result;
    }

    public static MapResult mapResult(int code) {
        return mapResult(code, null);
    }

    public static void streamResult(OutputStream stream, int code, String message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartObject();

        json.writeNumberField("code", code);
        json.writeStringField("message", message);

        json.writeEndObject();
        json.flush();
        json.close();
    }

    public static void streamResult(OutputStream stream, int code, String message, WriteData writeData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
        json.writeStartObject();

        json.writeNumberField("code", code);
        json.writeStringField("message", message);

        writeData.write(json);

        json.writeEndObject();
        json.flush();
        json.close();
    }

    public interface WriteData {
        void write(JsonGenerator json) throws Exception;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
