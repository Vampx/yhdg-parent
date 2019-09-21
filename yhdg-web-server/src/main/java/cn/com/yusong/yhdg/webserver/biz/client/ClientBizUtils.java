package cn.com.yusong.yhdg.webserver.biz.client;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.*;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.entity.result.DataResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientBizUtils {

    static AtomicInteger sequence = new AtomicInteger();
    public static Map<Integer, RespBody> respBodyPool = new ConcurrentHashMap<Integer, RespBody>();
    public static Map<String, RespBody> respVehicleBodyPool = new ConcurrentHashMap<String, RespBody>();
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
            return SerialResult.result(TsRespCode.CODE_4.getValue(), TsRespCode.CODE_4.getName(), null, message.getSerial());

        } else if (resp.rtnCode == TsRespCode.CODE_0.getValue()) {
            return SerialResult.result(resp.rtnCode, null, resp, message.getSerial());

        } else if (resp.rtnCode == TsRespCode.CODE_3.getValue()) {
            return SerialResult.result(resp.rtnCode, TsRespCode.CODE_3.getName(), null, -1);

        } else if (resp.rtnCode == TsRespCode.CODE_4.getValue()) {
            return SerialResult.result(resp.rtnCode, TsRespCode.CODE_4.getName(), null, -1);

        } else {
            throw new IllegalArgumentException();
        }
    }


    public static SerialResult lockVehicle(AppConfig config, String vinNo, int status) throws InterruptedException {

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_VEHICLE_ID_V_LOGIN_SERVER, vinNo));
        if (StringUtils.isEmpty(key)) {
            return SerialResult.result(TsRespCode.CODE_3.getValue(), "车辆终端不在线", null,-1);
        }

        Msg_WEB_11 message = new Msg_WEB_11();
        message.time = DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT);
        message.terminalType = "0";
        message.version = "";
        message.vinNo = vinNo;
        message.status = status;
        Msg_WEB_12 resp = null;

        try {
            RespBody<Msg_WEB_12> respBody = new RespBody<Msg_WEB_12>(RESP_WAIT_TIME);
            respVehicleBodyPool.put(message.vinNo, respBody);
            if (config.vehicleServerClientManager.writeAndFlush(key, message) == null) {
                return SerialResult.result(TsRespCode.CODE_5.getValue(), "与业务服务器连接失败", null, -1);
            }
            resp = respBody.getBody();
        } finally {
            respVehicleBodyPool.remove(message.vinNo);
        }

        if (resp == null) {
            return SerialResult.result(TsRespCode.CODE_4.getValue(), TsRespCode.CODE_4.getName(), null, -1);

        } else if (resp.code == TsRespCode.CODE_0.getValue()) {
            return SerialResult.result(resp.code, null, resp, -1);

        } else if (resp.code == TsRespCode.CODE_3.getValue()) {
            return SerialResult.result(resp.code, TsRespCode.CODE_3.getName(), null, -1);

        } else if (resp.code == TsRespCode.CODE_4.getValue()) {
            return SerialResult.result(resp.code, TsRespCode.CODE_4.getName(), null, -1);

        } else if (resp.code == TsRespCode.CODE_8.getValue()) {
            return SerialResult.result(resp.code, TsRespCode.CODE_8.getName(), null, -1);

        } else {
            throw new IllegalArgumentException();
        }
    }

    public static SerialResult quryParameter(AppConfig config, String vinNo, String value) throws InterruptedException {

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_VEHICLE_ID_V_LOGIN_SERVER, vinNo));
        if (StringUtils.isEmpty(key)) {
            return SerialResult.result(TsRespCode.CODE_3.getValue(), "车辆终端不在线", null,-1);
        }

        Msg_WEB_21 message = new Msg_WEB_21();
        message.time = DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT);
        message.terminalType = "0";
        message.version = "";
        message.vinNo = vinNo;
        message.value = value;
        Msg_WEB_22 resp = null;

        try {
            RespBody<Msg_WEB_22> respBody = new RespBody<Msg_WEB_22>(RESP_WAIT_TIME);
            respVehicleBodyPool.put(message.vinNo, respBody);
            if (config.vehicleServerClientManager.writeAndFlush(key, message) == null) {
                return SerialResult.result(TsRespCode.CODE_5.getValue(), "与业务服务器连接失败", null, -1);
            }
            resp = respBody.getBody();
        } finally {
            respVehicleBodyPool.remove(message.vinNo);
        }

        if (resp == null) {
            return SerialResult.result(TsRespCode.CODE_4.getValue(), TsRespCode.CODE_4.getName(), null, -1);

        } else if (resp.returnValue != null) {
            return SerialResult.result(TsRespCode.CODE_0.getValue(), null, resp, -1);

        } else {
            throw new IllegalArgumentException();
        }
    }

    public static SerialResult setParameter(AppConfig config, String vinNo, String value) throws InterruptedException {

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_VEHICLE_ID_V_LOGIN_SERVER, vinNo));
        if (StringUtils.isEmpty(key)) {
            return SerialResult.result(TsRespCode.CODE_3.getValue(), "车辆终端不在线", null,-1);
        }

        Msg_WEB_31 message = new Msg_WEB_31();
        message.time = DateFormatUtils.format(new Date(), Constant.DATE_TIME_FORMAT);
        message.terminalType = "0";
        message.version = "";
        message.vinNo = vinNo;
        message.value = value;
        Msg_WEB_32 resp = null;

        try {
            RespBody<Msg_WEB_32> respBody = new RespBody<Msg_WEB_32>(RESP_WAIT_TIME);
            respVehicleBodyPool.put(message.vinNo, respBody);
            if (config.vehicleServerClientManager.writeAndFlush(key, message) == null) {
                return SerialResult.result(TsRespCode.CODE_5.getValue(), "与业务服务器连接失败", null, -1);
            }
            resp = respBody.getBody();
        } finally {
            respVehicleBodyPool.remove(message.vinNo);
        }

        if (resp == null) {
            return SerialResult.result(TsRespCode.CODE_4.getValue(), TsRespCode.CODE_4.getName(), null, -1);

        } else if (resp.code == 1) {
            return SerialResult.result(TsRespCode.CODE_0.getValue(), null, resp, -1);

        } else if (resp.code == 0) {
            return SerialResult.result(TsRespCode.CODE_9.getValue(), null, resp, -1);

        }  else {
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