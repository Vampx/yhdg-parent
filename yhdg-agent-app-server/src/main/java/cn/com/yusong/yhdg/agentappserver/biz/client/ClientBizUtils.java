package cn.com.yusong.yhdg.agentappserver.biz.client;


import cn.com.yusong.yhdg.agentappserver.config.AppConfig;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000005;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000005;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientBizUtils {

    static AtomicInteger sequence = new AtomicInteger();
    public static Map<Integer, RespBody> respBodyPool = new ConcurrentHashMap<Integer, RespBody>();
    static final long RESP_WAIT_TIME = 1000 * 3; //10秒

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

    public static SerialResult openStandardBox(AppConfig config, String cabinetId, String boxNum, int subtype) throws InterruptedException {

        byte lockNum = Byte.parseByte(boxNum, 10);
        byte boxType = (byte) (subtype == Cabinet.Subtype.EXCHANGE_CHARGE.getValue() ? 0 : 1);

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_SUBCABINET_ID_V_LOGIN_SERVER, cabinetId));
        if (StringUtils.isEmpty(key)) {
            return SerialResult.result(TsRespCode.CODE_3.getValue(), "终端不在线", null, -1);
        }

        Msg221000004 message = new Msg221000004();
        message.cabinetId = cabinetId;
        message.lockNum = lockNum;
        message.boxType = boxType;
        message.setSerial(sequence.incrementAndGet());
        Msg222000004 resp = null;

        try {
            RespBody<Msg222000004> respBody = new RespBody<Msg222000004>(RESP_WAIT_TIME);
            respBodyPool.put(message.getSerial(), respBody);
            if (config.cabinetServerClientManager.writeAndFlush(key, message) == null) {
                return SerialResult.result(TsRespCode.CODE_5.getValue(), "与业务服务器连接失败", null, -1);
            }
            resp = respBody.getBody();
        } finally {
            respBodyPool.remove(message.getSerial());
        }

        if (resp == null) {
            return SerialResult.result(TsRespCode.CODE_4.getValue(), null, null, message.getSerial());

        } else if (resp.rtnCode == TsRespCode.CODE_0.getValue()) {
                return SerialResult.result(resp.rtnCode, null, resp, message.getSerial());
        } else if (resp.rtnCode == TsRespCode.CODE_3.getValue()) {
            return SerialResult.result(resp.rtnCode, null, null, -1);

        } else if (resp.rtnCode == TsRespCode.CODE_4.getValue()) {
            return SerialResult.result(resp.rtnCode, null, null, -1);

        } else {
            throw new IllegalArgumentException();
        }
    }

    public static RestResult queryBox(AppConfig config, String cabinetId) throws InterruptedException {
        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_SUBCABINET_ID_V_LOGIN_SERVER, cabinetId));
        if (StringUtils.isEmpty(key)) {
            return RestResult.result(TsRespCode.CODE_1.getValue(), "终端不在线");
        }

        Msg221000005 message = new Msg221000005();
        message.cabinetId = cabinetId;
        message.setSerial(sequence.incrementAndGet());
        Msg222000005 resp = null;

        try {
            RespBody<Msg222000005> respBody = new RespBody<Msg222000005>(RESP_WAIT_TIME);
            respBodyPool.put(message.getSerial(), respBody);
            if (config.cabinetServerClientManager.writeAndFlush(key, message) == null) {
                return RestResult.result(TsRespCode.CODE_1.getValue(), "与业务服务器连接失败");
            }
            resp = respBody.getBody();
        } finally {
            respBodyPool.remove(message.getSerial());
        }

        if (resp == null) {
            return SerialResult.result(TsRespCode.CODE_4.getValue(), null, null, message.getSerial());

        } else if (resp.rtnCode == TsRespCode.CODE_0.getValue()) {
            return SerialResult.result(resp.rtnCode, null, resp, message.getSerial());

        } else if (resp.rtnCode == TsRespCode.CODE_3.getValue()) {
            return SerialResult.result(resp.rtnCode, null, null, -1);

        } else if (resp.rtnCode == TsRespCode.CODE_4.getValue()) {
            return SerialResult.result(resp.rtnCode, null, null, -1);

        } else {
            throw new IllegalArgumentException();
        }
    }

    public static class SerialResult extends DataResult {
        int serial = -1;

        protected SerialResult(int code, String message, Object data) {
            super(code, message, data);
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public static SerialResult result(int code, String message, Object data, int serial) {
            SerialResult result = new SerialResult(code, message, data);
            result.setSerial(serial);
            return result;
        }
    }

}