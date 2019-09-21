package cn.com.yusong.yhdg.staticserver.biz.client;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.entity.result.DataResult;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientBizUtils {

    private final static Logger log = LogManager.getLogger(ClientBizUtils.class);
    public static Map<Integer, RespBody> respBodyPool = new ConcurrentHashMap<Integer, RespBody>();
    static final long RESP_WAIT_TIME = 1000 * 3; //10秒

    static AtomicInteger sequence = new AtomicInteger();

    public static Map<Integer, Resp> respPool = new ConcurrentHashMap<Integer, Resp>();
    public static final Runnable SCAN_TASK = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            List<Integer> expireList = new LinkedList<Integer>();
            for(Map.Entry<Integer, Resp> kv : respPool.entrySet()) {
                if(now - kv.getValue().timestamp >= 1000 * 15) {
                    expireList.add(kv.getKey());
                }
            }
            for(Integer k : expireList) {
                respPool.remove(k);
            }

            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e) {
            }
        }
    };

    public static class Resp {
        public Object data;
        public long timestamp = System.currentTimeMillis();

        public Resp(Object data) {
            this.data = data;
        }
    }

    public static SerialResult openStandardBox(AppConfig config, String cabinetId, String boxNum, int subtype) throws InterruptedException {

        byte lockNum = Byte.parseByte(boxNum, 10);
        byte boxType = (byte) (subtype == Cabinet.Subtype.EXCHANGE_CHARGE.getValue() ? 0 : 1);

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_SUBCABINET_ID_V_LOGIN_SERVER, cabinetId));
        if (StringUtils.isEmpty(key)) {
            if(log.isDebugEnabled()) {
                log.debug("终端{} 不在线", cabinetId);
            }

            return SerialResult.result(TsRespCode.CODE_3.getValue(), "终端不在线", null,-1);
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
