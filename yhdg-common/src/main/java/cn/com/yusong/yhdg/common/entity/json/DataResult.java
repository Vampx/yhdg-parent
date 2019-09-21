package cn.com.yusong.yhdg.common.entity.json;

public class DataResult extends ExtResult {
	
	private Object data;

    public static DataResult successResult(Object data) {
        DataResult result = new DataResult();
        result.success = true;
        result.timeout = false;
        result.licence = true;

        result.data = data;

        return result;
    }

	public static DataResult successResult(String message, Object data) {
		DataResult result = new DataResult();
		result.success = true;
		result.timeout = false;
		result.licence = true;
		
		result.message = message;
		result.data = data;
		
		return result;
	}

    public static DataResult failResult(String message, Object data) {
        DataResult result = new DataResult();
        result.success = false;
        result.timeout = false;
        result.licence = true;

        result.message = message;
        result.data = data;

        return result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
