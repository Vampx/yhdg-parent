package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalDownloadProgress;
import cn.com.yusong.yhdg.common.domain.yms.TerminalOnline;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000003;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000003;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalDownloadProgressMapper;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalDownloadProgressService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalOnlineService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 终端心跳
 */
@Component
public class Biz921000003 extends AbstractBiz {

    static Logger log = LoggerFactory.getLogger(Biz921000003.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalOnlineService terminalOnlineService;
    @Autowired
    TerminalDownloadProgressService terminalDownloadProgressService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000003 request = (Msg921000003) message;
        Msg922000003 response = new Msg922000003();
        response.setSerial(request.getSerial());

        TerminalSession session = getSession(attributes);

        if(session == null) {
            response.rtnCode = RespCode.CODE_7.getValue();
            response.rtnMsg = RespCode.CODE_7.getName();
            writeAndFlush(context, response);
            return;
        }
        log.debug("terminal ID {} ", session.id);

        Terminal terminal = terminalService.find(session.id);
        if(terminal == null) {
            response.rtnCode = RespCode.CODE_3.getValue();
            response.rtnMsg = RespCode.CODE_3.getName();
            writeAndFlush(context, response);
            return;
        }

        session.refreshHeartTime();
        String serverAddress = config.getServerIp() + ":" + config.getServerPort();
        memCachedClient.set(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, session.id), serverAddress, MemCachedConfig.CACHE_FIVE_MINUTE);

        TerminalOnline online = new TerminalOnline();
        online.setId(session.id);
        online.setCpu(request.cpu == Float.NaN ? 0 : request.cpu);
        online.setMemory(request.memory);
        online.setCardCapacity(request.cardCapacity);
        online.setRestCapacity(request.restCapacity);
        online.setNewworkSignal((int) request.signal);
        online.setPlayVolume((int) request.playVolume);
        online.setStrategyUid(request.strategyUid);
        online.setHeartTime(new Date());
        online.setIsOnline(ConstEnum.Flag.TRUE.getValue());

        int effect = terminalOnlineService.updateHeartInfo(online);
        log.debug("terminalOnline update result {} ", effect);
        if (effect == 0) {
            if (log.isDebugEnabled()) {
                log.debug("TerminalOnline {} not exist, insert terminalOnline", online.getId());
            }

            terminalOnlineService.insert(online);

            if (terminalDownloadProgressService.find(session.id) == null) {
                TerminalDownloadProgress progress = new TerminalDownloadProgress();
                progress.setId(session.id);
                terminalDownloadProgressService.insert(progress);
            }
        }

        writeAndFlush(context, response);
    }
}
