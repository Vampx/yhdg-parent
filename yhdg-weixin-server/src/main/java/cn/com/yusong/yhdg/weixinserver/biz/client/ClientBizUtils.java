package cn.com.yusong.yhdg.weixinserver.biz.client;

import cn.com.yusong.yhdg.common.entity.RespBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ClientBizUtils {

    static AtomicLong sequence = new AtomicLong();
    public static Map<Long, RespBody> respBodyPool = new ConcurrentHashMap<Long, RespBody>();
    static final long RESP_WAIT_TIME = 1000 * 10; //10秒

    public static class Resp<T> {
        public static final byte CODE_OK = 0;
        public static final byte CODE_NO_CLIENT = 1;
        public static final byte CODE_RESP_TIMEOUT = 2;

        public static final Resp RESP_NO_CLIENT = new Resp(CODE_NO_CLIENT, null);
        public static final Resp RESP_TIMEOUT = new Resp(CODE_RESP_TIMEOUT, null);

        public byte code;
        public T data;

        public Resp(byte code, T data) {
            this.code = code;
            this.data = data;
        }
    }

}
