package cn.com.yusong.yhdg.batteryserver.biz.client;


import cn.com.yusong.yhdg.batteryserver.comm.client.ServiceServerClient;
import cn.com.yusong.yhdg.batteryserver.config.AppConfig;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg21.Msg211000001;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientBizUtils {

    static AtomicInteger sequence = new AtomicInteger();
    public static Map<Integer, RespBody> respBodyPool = new ConcurrentHashMap<Integer, RespBody>();

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

    public static <T> Resp<T> sendMobileMessage(AppConfig config, String mobile, String content, int partnerId, byte type, String variable, String templateCode) throws InterruptedException {
        Msg211000001 msg = new Msg211000001();
        msg.setSerial(sequence.incrementAndGet());
        msg.partnerId = partnerId;
        msg.mobile = mobile;
        msg.content = content;
        msg.type = type;
        msg.variable = variable;
        msg.templateCode = templateCode;

        ServiceServerClient client = config.serviceServerClientManager.getOneClient();
        if (client == null) {
            return Resp.RESP_NO_CLIENT;
        }
        client.getChannel().writeAndFlush(msg);

        return new Resp(Resp.CODE_OK, null);
    }

}