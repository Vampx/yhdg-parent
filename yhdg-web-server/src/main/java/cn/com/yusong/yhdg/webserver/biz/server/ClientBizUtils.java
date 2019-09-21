package cn.com.yusong.yhdg.webserver.biz.server;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.protocol.msg93.*;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBizUtils {

    static final long RESP_WAIT_TIME = 1000 * 5; //5秒

    public static boolean noticeStrategyChanged(AppConfig config, String uid, List<String> terminalList) {
        return false;
    }

    public static void noticePlaylistUpdate(AppConfig config, Msg931000003 msg) {

    }


    public static boolean noticeNewCommand(AppConfig config, String terminalId, Msg931000008 msg) {
        return false;

    }

    public static boolean noticeCabinetConfigUpdate(AppConfig config, String terminalId, String address, String tel) {
        return false;
    }
}
