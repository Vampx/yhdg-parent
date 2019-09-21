package cn.com.yusong.yhdg.agentserver.biz.server;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.protocol.msg93.*;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBizUtils {

    static final long RESP_WAIT_TIME = 1000 * 5; //5秒
    protected static Map<Long, RespBody> respBodyPool = new ConcurrentHashMap<Long, RespBody>();

    public static boolean noticeStrategyChanged(AppConfig config, String uid, List<String> terminalList) {
        for (String id : terminalList) {
            String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, id));
            if(StringUtils.isEmpty(key)) {
                return false;
            }

            Msg931000001 msg = new Msg931000001();
            msg.setSerial((int) config.sequence.incrementAndGet());
            msg.uid = uid;
            msg.terminalId = id;
            return config.frontServerClientManager.writeAndFlush(key, msg) != null;
        }
        return true;
    }

    public static void noticePlaylistUpdate(AppConfig config, Msg931000003 msg) {
        msg.setSerial((int) config.sequence.incrementAndGet());
        config.frontServerClientManager.writeAndFlush(msg);
    }

    public static boolean noticeTerminalConfigUpdate(AppConfig config, String terminalId, Msg931000007 msg) {
        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, terminalId));
        if(StringUtils.isEmpty(key)) {
            return false;
        }

        msg.setSerial((int) config.sequence.incrementAndGet());
        msg.terminalId = terminalId;

        return config.frontServerClientManager.writeAndFlush(key, msg) != null;
    }

    public static boolean noticeNewCommand(AppConfig config, String terminalId, Msg931000008 msg) {
        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, terminalId));
        if(StringUtils.isEmpty(key)) {
            return false;
        }

        msg.setSerial((int) config.sequence.incrementAndGet());
        return config.frontServerClientManager.writeAndFlush(key, msg) != null;
    }

    public static ExtResult noticeTerminalLoad(AppConfig config, Long logId, String terminalId, Integer type, String logTime) {
        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, terminalId));
        if (StringUtils.isEmpty(key)) {
            return ExtResult.failResult("终端不在线！");
        }

        Msg931000009 msg = new Msg931000009();
        msg.terminalId = terminalId;
        msg.logId = logId.intValue();
        msg.type = type;
        msg.logTime = logTime;
        msg.setSerial((int) config.sequence.incrementAndGet());
        if(config.frontServerClientManager.writeAndFlush(key, msg) == null) {
            return ExtResult.failResult("与业务服务器连接失败");
        }
        return ExtResult.successResult();
    }

    public static boolean noticeCabinetConfigUpdate(AppConfig config, String terminalId, String address, String tel) {
        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, terminalId));
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        Msg931000010 msg = new Msg931000010();
        msg.terminalId = terminalId;
        msg.address = address;
        msg.tel = tel;
        msg.setSerial((int) config.sequence.incrementAndGet());

        return config.frontServerClientManager.writeAndFlush(key, msg) != null;
    }
}
