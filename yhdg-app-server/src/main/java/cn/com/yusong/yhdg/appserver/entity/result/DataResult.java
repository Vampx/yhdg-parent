package cn.com.yusong.yhdg.appserver.entity.result;


public class DataResult extends RestResult {
    Object data;

    protected DataResult(int code, String message, Object data) {
        super(code, message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
