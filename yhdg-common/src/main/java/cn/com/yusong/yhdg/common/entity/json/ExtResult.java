package cn.com.yusong.yhdg.common.entity.json;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

public class ExtResult {
	
	boolean success;
	String message;
	boolean timeout;
	boolean licence;
	
	public static ExtResult successResult() {
		return successResult(null);
    }

	public static ExtResult successResult(String message) {
		ExtResult result = new ExtResult();
		result.success = true;
		result.timeout = false;
		result.licence = true;

		result.message = message;
		return result;
	}

	public static ExtResult failResult(String message) {
		ExtResult result = new ExtResult();

		result.success = false;
		result.timeout = false;
		result.licence = true;

		result.message = message;
		return result;
	}

	public static ExtResult timeoutResult() {
		ExtResult result = new ExtResult();

		result.success = false;
		result.timeout = true;
		result.licence = true;
        result.message = "用户超时";

		return result;
	}

	public static ExtResult licenceFailResult() {
		
		ExtResult result = new ExtResult();
		
		result.success = false;
		result.timeout = false;
		result.licence = false;
        result.message = "许可证错误";
		
		return result;
	}

	public static void streamResult(OutputStream stream, boolean success, boolean timeout, boolean licence, String message) throws Exception {
		streamResult(stream, success, timeout, licence, message, new DefaultWriteData());
	}
	
	public static void streamResult(OutputStream stream, boolean success, boolean timeout, boolean licence, String message, WriteData writeData) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonGenerator json = objectMapper.getJsonFactory().createJsonGenerator(stream, JsonEncoding.UTF8);
		
		json.writeStartObject();
		json.writeBooleanField("success", success);
		json.writeBooleanField("timeout", timeout);
		json.writeBooleanField("licence", licence);
		json.writeStringField("message", message);
		
		writeData.writeData(json);
		
		json.writeEndObject();
		
		json.flush();
		json.close();
	}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public boolean isLicence() {
        return licence;
    }

    public void setLicence(boolean licence) {
        this.licence = licence;
    }

    public static interface WriteData {
		public void writeData(JsonGenerator json) throws JsonGenerationException, IOException, InterruptedException;
	}
	
	static class DefaultWriteData implements WriteData {
		@Override
		public void writeData(JsonGenerator json)
				throws JsonGenerationException, IOException {
		}
	}
}
