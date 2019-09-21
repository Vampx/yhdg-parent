package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.protocol.msg92.Interface922000002;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000002;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000002;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.frontserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.frontserver.service.yms.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 终端登录
 */
@Component
public class Biz921000002 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000002.class);

    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalOnlineService terminalOnlineService;
    @Autowired
    TerminalStrategyService terminalStrategyService;
    @Autowired
    TerminalPropertyService terminalPropertyService;
    @Autowired
    PlaylistService playlistService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    TerminalCodeService terminalCodeService;
    @Autowired
    TerminalSequenceService terminalSequenceService;
    @Autowired
    CabinetService cabinetService;


    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000002 request = (Msg921000002) message;
        Msg922000002 response = new Msg922000002();
        response.setSerial(request.getSerial());

        String source = String.format("%s%s%s%s", request.uid, request.version, request.code, request.ip);
        String validateCode = CodecUtils.md5(source);

        if(!validateCode.equalsIgnoreCase(request.validateCode)) {
            response.rtnCode = RespCode.CODE_4.getValue();
            response.rtnMsg = RespCode.CODE_4.getName();
            writeAndFlush(context, response);
            return;
        }

        if(StringUtils.isEmpty(request.uid)) {
            response.rtnCode = RespCode.CODE_2.getValue();
            response.rtnMsg = RespCode.CODE_2.getName();
            writeAndFlush(context, response);
            return;
        }

        TerminalCode terminalCode = terminalCodeService.find(request.uid);
        String code = null;
        if (terminalCode == null) {
            code = terminalSequenceService.nextCode();

            TerminalCode entity = new TerminalCode();
            entity.setId(request.uid);
            entity.setCode(code);
            terminalCodeService.insert(entity);
        }else {
            code= terminalCode.getCode();
        }

        log.debug("terminal ID {} ", code);
        if (terminalService.find(code) == null) {
            Terminal terminal = new Terminal();
            terminal.setId(code);
            terminal.setVersion(request.version);
            terminal.setUid(request.uid);
            terminal.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            terminal.setHeartTime(new Date());

            TerminalOnline online = new TerminalOnline();
            online.setId(code);
            online.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            online.setIsNormal(ConstEnum.Flag.TRUE.getValue());
            online.setHeartTime(new Date());

            TerminalDownloadProgress progress = new TerminalDownloadProgress();
            progress.setId(code);
            terminalService.create(terminal, online, progress);
        }

        int effect = terminalService.updateLoginInfo(code, request.version);
        if(effect > 0) {
            terminalOnlineService.updateLoginInfo(code, ConstEnum.Flag.TRUE.getValue(), new Date());
        }

        Terminal terminal = terminalService.find(code);
        Cabinet cabinet = cabinetService.findTerminal(code);
        response.terminalName = cabinet != null ? cabinet.getCabinetName() : code;
        response.id = terminal.getId();

        String serverAddress = config.getServerIp() + ":" + config.getServerPort();
        memCachedClient.set(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, terminal.getId()), serverAddress, MemCachedConfig.CACHE_FIVE_MINUTE);
        TerminalSession terminalSession = config.sessionManager.addTerminalSession(context, attributes, terminal.getId());

        if(terminal.getStrategyId() != null) {
            TerminalStrategy strategy = terminalStrategyService.find(terminal.getStrategyId());
            response.strategyUid = String.format("strategy-%d-%d", terminal.getStrategyId(), strategy.getVersion());
        }

        response.logLevel = (byte) Terminal.LogLevel.NULL.getValue();
        response.ftpEncoding = systemConfigService.find(ConstEnum.SystemConfigKey.FTP_ENCODING.getValue()).getConfigValue();
        if(terminal.getPlaylistId() != null){
            terminalSession.playlistId = terminal.getPlaylistId().intValue();
            response.playlistId = terminal.getPlaylistId().intValue();
            response.playlistName = playlistService.find(terminal.getPlaylistId()).getPlaylistName();
        } else {
            response.playlistId = 0;
        }

        response.ftpUser = systemConfigService.find(ConstEnum.SystemConfigKey.FTP_USER.getValue()).getConfigValue();
        response.ftpPassword = systemConfigService.find(ConstEnum.SystemConfigKey.FTP_PASSWORD.getValue()).getConfigValue();
        response.ftpPort = Integer.parseInt(systemConfigService.find(ConstEnum.SystemConfigKey.FTP_PORT.getValue()).getConfigValue());

        List<TerminalProperty> properties = terminalPropertyService.findByTerminal(code);
        for(TerminalProperty property : properties) {
            if(property.getIsActive() == ConstEnum.Flag.TRUE.getValue()) {
                response.propertyList.add(new Interface922000002.Property(property.getPropertyName(), property.getPropertyValue()));
            }
        }

        writeAndFlush(context, response);
    }
}
