package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.hdg;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.TerminalUpgradePackDetailService;
import cn.com.yusong.yhdg.appserver.service.basic.TerminalUpgradePackService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.yms.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.*;
import cn.com.yusong.yhdg.common.entity.QrcodeResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_cabinet_app_hdg_cabinet_app")
@RequestMapping(value = "/api/v1/cabinet_app/hdg/cabinet_app")
public class CabinetAppController extends ApiController {

    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalCodeService terminalCodeService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalSequenceService terminalSequenceService;
    @Autowired
    TerminalOnlineService terminalOnlineService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;
    @Autowired
    TerminalUploadLogService terminalUploadLogService;
    @Autowired
    protected AppConfig config;
    @Autowired
    PlaylistService playlistService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginParam {
        @NotBlank(message = "mac不能为空")
        public String mac;
        public String version;
        public Integer playlistId;
        public String playlistVersion;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/login")
    public RestResult login(@Valid @RequestBody LoginParam param) {
//        if (config.getExpiryTime() != null && config.getExpiryTime() < new Date().getTime()) {
//            return RestResult.result(RespCode.CODE_8);
//        }
        TerminalCode terminalCode = terminalCodeService.find(param.mac);
        if (terminalCode == null) {
            String code = terminalSequenceService.nextCode();
            terminalCode = new TerminalCode();
            terminalCode.setId(param.mac);
            terminalCode.setCode(code);
            terminalCodeService.insert(terminalCode);
        }
        Playlist playlist = null;
        Terminal terminal = terminalService.find(terminalCode.getCode());
        if (terminal == null) {
            terminal = new Terminal();
            terminal.setId(terminalCode.getCode());
            terminal.setUid(param.mac);
            terminal.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            terminal.setVersion(param.version);
            if(param.playlistId != null && param.playlistId !=0){
                terminal.setPlaylistId(param.playlistId);
            }
            terminal.setPlaylistVersion(param.playlistVersion);
            terminal.setHeartPlaylistId(param.playlistId);
            terminal.setHeartPlaylistVersion(param.playlistVersion);
            terminal.setHeartTime(new Date());
            terminalService.insert(terminal);

//            TerminalOnline terminalOnline = new TerminalOnline();
//            terminalOnline.setId(terminalCode.getCode());
//            terminalOnline.setIsNormal(ConstEnum.Flag.TRUE.getValue());
//            terminalOnline.setIsOnline(ConstEnum.Flag.TRUE.getValue());
//            terminalOnlineService.insert(terminalOnline);
        } else {
            terminal.setHeartTime(new Date());
            terminal.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            terminal.setVersion(param.version);

            terminal.setHeartPlaylistId(param.playlistId);
            terminal.setHeartPlaylistVersion(param.playlistVersion);
            terminalService.updateOnline(terminal);
        }

        Cabinet cabinet = cabinetService.findByTerminalId(terminal.getId());
        Map map = new HashMap();
        map.put("id", terminal.getId());

        if (cabinet == null) {
            map.put("cabinetId", "");
            map.put("token", "");
            map.put("expireIn", -1);
        } else {
            if (StringUtils.isNotEmpty(cabinet.getLoginToken())) {
                tokenCache.remove(cabinet.getLoginToken());
            }

            int expireIn = MemCachedConfig.CACHE_ONE_DAY;
            String token = tokenCache.putCabinetApp(terminal.getId(), cabinet.getId(), expireIn).token;
            // 插入token到cabinet
            cabinetService.updateToken(cabinet.getId(), token);

            map.put("cabinetId", cabinet.getId());
            map.put("token", token);
            map.put("expireIn", expireIn);

            Agent agent = agentService.find(cabinet.getAgentId());
            String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
            map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_CABINET.getFormat(), url, cabinet.getId()));

            map.put("ftpUser", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FTP_USER.getValue()));
            map.put("ftpPassword",  systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FTP_PASSWORD.getValue()));
            map.put("ftpServer",  systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FTP_SERVER.getValue()));
            map.put("ftpPost", Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FTP_PORT.getValue())));
            map.put("ftpEncoding", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.FTP_ENCODING.getValue()));
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpgradeParam {
        public String version;
    }

    @ResponseBody
    @RequestMapping(value = "/upgrade")
    public RestResult upgrade(@Valid @RequestBody UpgradeParam param) {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        Map map = new HashMap();

        //查询是否有屏幕升级
        if(cabinet.getTerminalId() != null && param.version != null){
            Terminal terminal = terminalService.find(cabinet.getTerminalId());
            if(terminal != null && terminal.getVersion() != null){
                List<TerminalUpgradePack> upgradePackList = terminalUpgradePackService.findByOldVersion(TerminalUpgradePack.PackType.TERMINAL_UPGRADE.getValue(), param.version);
                for(TerminalUpgradePack upgradePack : upgradePackList) {
                    TerminalUpgradePackDetail detail = terminalUpgradePackDetailService.find(upgradePack.getId(), terminal.getId());
                    if (detail != null && upgradePack.getNewVersion() != null && !param.version.equals(upgradePack.getNewVersion())) {
                        map.put("filePath", config.staticUrl + upgradePack.getFilePath() );
                        map.put("version",upgradePack.getNewVersion() );
                    }
                }
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(),null,map);
    }

    @ResponseBody
    @RequestMapping(value = "/updateLog")
    public RestResult updateLog() {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        Map map = new HashMap();

        TerminalUploadLog terminalUploadLog = terminalUploadLogService.findLastByTerminal(cabinet.getTerminalId(), TerminalUploadLog.Status.NOTICE.getValue());
        if(terminalUploadLog != null){
            map.put("logId", terminalUploadLog.getId());
            map.put("staticUrl", config.staticUrl);
            map.put("type", terminalUploadLog.getType());
            map.put("logTime",terminalUploadLog.getLogTime() );
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(),null,map);
    }
}
