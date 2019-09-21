package cn.com.yusong.yhdg.agentappserver.entity.result;

import java.util.HashMap;
import java.util.Map;

public class MapResult extends RestResult {

    Map<String, Object> data = new HashMap<String, Object>();

    protected MapResult(int code, String message) {
        super(code, message);
    }

    public Object getData() {
        return data;
    }

    public MapResult put(String k, Object v) {
        data.put(k, v);
        return this;
    }

    public MapResult putAll(Map<String, Object> map) {
        data.putAll(map);
        return this;
    }
}
