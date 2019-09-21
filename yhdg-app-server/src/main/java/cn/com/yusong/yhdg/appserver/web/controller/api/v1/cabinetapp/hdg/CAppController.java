package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.appserver.service.yms.TerminalUploadLogService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_c_app")
@RequestMapping(value = "/api/v1/c_app")
public class CAppController extends ApiController {

    private static final Logger log = LogManager.getLogger(CAppController.class);

    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;
    @Autowired
    TerminalUploadLogService terminalUploadLogService;
    @Autowired
    DeviceUpgradePackService deviceUpgradePackService;
    @Autowired
    DeviceCommandService deviceCommandService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    ZhizuCustomerNoticeService zhizuCustomerNoticeService;

    @ResponseBody
    @RequestMapping(value = "/c")
    public RestResult c() {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        Map map = new HashMap();
        if(cabinet.getOperationFlag()==null){
            cabinet.setOperationFlag(0);
        }
        map.put("flag",cabinet.getOperationFlag());
        cabinetService.updateOperationFlag(cabinet.getId(), ConstEnum.Flag.FALSE.getValue());

        if(cabinet.getTerminalId() != null){
            Terminal terminal = terminalService.find(cabinet.getTerminalId());
            if(terminal != null) {
                Date now = new Date();
                if (terminal.getIsOnline() == ConstEnum.Flag.FALSE.getValue()) {
                    terminal.setIsOnline(ConstEnum.Flag.TRUE.getValue());
                    terminal.setHeartTime(now);
                    terminalService.updateOnline(terminal);
                }
                //如果屏幕心跳和当前时间相差两分钟更新一次心跳时间
                if (terminal.getHeartTime() != null && terminal.getHeartTime().compareTo(DateUtils.addMinutes(now, -2)) < 0) {
                    terminal.setHeartTime(now);
                    terminalService.updateHeartTime(terminal);
                }

                boolean notice = false;

                //查询屏幕  是否有屏幕升级
                if (terminal != null && terminal.getVersion() != null) {
                    List<TerminalUpgradePack> upgradePackList = terminalUpgradePackService.findByOldVersion(TerminalUpgradePack.PackType.TERMINAL_UPGRADE.getValue(), terminal.getVersion());
                    for (TerminalUpgradePack upgradePack : upgradePackList) {
                        TerminalUpgradePackDetail detail = terminalUpgradePackDetailService.find(upgradePack.getId(), terminal.getId());
                        if (detail != null && upgradePack.getNewVersion() != null && !terminal.getVersion().equals(upgradePack.getNewVersion())) {
                            notice = true;
                            map.put("notice", 1);
                        }
                    }
                }

                //查询是否有日志上传
                if (!notice) {
                    List<TerminalUploadLog> terminalUploadLogList = terminalUploadLogService.findByTerminal(cabinet.getTerminalId(), TerminalUploadLog.Status.INIT.getValue());
                    for (TerminalUploadLog log : terminalUploadLogList) {
                        notice = true;
                        terminalUploadLogService.updateStatus(log.getId(), TerminalUploadLog.Status.NOTICE.getValue());
                    }
                    if (notice) {
                        map.put("notice", 2);
                    }
                }

                //查询是否有广告上传
                if (!notice) {
                    if(terminal.getHeartPlaylistId() == null ){
                        if( terminal.getPlaylistId() != null ){
                            notice = true;
                        }
                    }else {
                        //播放列表不同 或者 版本不一致
                        if(terminal.getPlaylistId() != null && (terminal.getHeartPlaylistId()  != terminal.getPlaylistId().intValue()
                                || (terminal.getHeartPlaylistVersion() == null || terminal.getHeartPlaylistVersion()  != terminal.getPlaylistVersion()))){
                            notice = true;
                        }
                    }

                    if (notice) {
                        map.put("notice", 3);
                        map.put("playlistId", terminal.getPlaylistId());
                        map.put("playlistVersion", terminal.getPlaylistVersion());
                    }
                }
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(),null,map);
    }

    @ResponseBody
    @RequestMapping(value = "/l")
    public RestResult l() {
        TokenCache.Data tokenData = getTokenData();
        Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "对应终端不存在");
        }
        Map map = new HashMap();

        ZhizuCustomerNotice zhizuCustomerNotice = zhizuCustomerNoticeService.find(tokenData.cabinetId);
        if(zhizuCustomerNotice != null){
            map.put("flag",1);
        }else{
            map.put("flag",0);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(),null,map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoopInfoParam {
        public int type;
        public String deviceId;
        public String version;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/loop_info")
    public RestResult loopInfo(@RequestBody LoopInfoParam param) {
        Map root = new HashMap();

        Device entity = deviceService.findByDeviceId(param.type, param.deviceId);
        if (entity != null) {
            if (!entity.getVersion().equals(param.version)) {
                entity.setVersion(param.version);
                entity.setHeartTime(new Date());
                deviceService.updateVersion(entity);
            }
        } else {
            Device device = new Device();
            device.setType(param.type);
            device.setDeviceId(param.deviceId);
            device.setVersion(param.version);
            device.setHeartTime(new Date());
            deviceService.insert(device);
        }

        DeviceUpgradePack upgradePack = deviceUpgradePackService.findByDevice(param.type, param.version, param.deviceId);
        if (upgradePack != null) {
            root.put("upgrade", 1);
            Map upgradeData = new HashMap();
            upgradeData.put("md5", upgradePack.getMd5Sum());
            upgradeData.put("filePath", staticImagePath(upgradePack.getFilePath()));
            upgradeData.put("version", upgradePack.getNewVersion());
            root.put("upgradeData", upgradeData);

        } else {
            root.put("upgrade", 0);
        }

        DeviceCommand deviceCommand = deviceCommandService.findOneByType(DeviceCommand.Type.REPORT_LOG.getValue(), param.deviceId, param.type, DeviceCommand.Status.NOT.getValue());
        if (deviceCommand != null) {
            root.put("uploadLog", 1);
            Map uploadLogData = new HashMap();
            uploadLogData.put("uploadLogUrl", staticImagePath("/security/upload/device_log.htm"));
            uploadLogData.put("logDate", deviceCommand.getLogDate());
            root.put("uploadLogData", uploadLogData);
            deviceCommandService.updateStatus(deviceCommand.getId(), DeviceCommand.Status.DISPATCHED.getValue(),new Date());
        } else {
            root.put("uploadLog", 0);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(),null, root);
    }
}
