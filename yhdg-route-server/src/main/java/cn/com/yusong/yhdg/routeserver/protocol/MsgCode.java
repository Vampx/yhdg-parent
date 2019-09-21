package cn.com.yusong.yhdg.routeserver.protocol;

import java.util.HashMap;
import java.util.Map;

public enum MsgCode {

    MSG_011000001(11000001, Msg011000001.class),
    MSG_011000003(11000003, Msg011000003.class),

    MSG_012000001(12000001, Msg012000001.class),
    MSG_012000003(12000003, Msg012000003.class),
    ;

    private static Map<Integer, Class> map = new HashMap<Integer, Class>();
    static {
        for(MsgCode e : MsgCode.values()) {
            map.put(e.code, e.clazz);
        }
    }

    private final int code;
    private final Class clazz;

    private MsgCode(int code, Class clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public final int getCode() {
        return code;
    }

    public static Class get(int code) {
        return map.get(code);
    }
}