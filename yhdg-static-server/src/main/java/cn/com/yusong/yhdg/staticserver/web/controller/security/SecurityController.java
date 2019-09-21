package cn.com.yusong.yhdg.staticserver.web.controller.security;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.service.hdg.CabinetOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;

public class SecurityController extends AbstractController {

    @Autowired
    AppConfig appConfig;

    @ModelAttribute
    public void addPid(String pid, Model model) {
        model.addAttribute("pid", pid);
    }

    @Autowired
    CabinetOperateLogService cabinetOperateLogService;

    protected void insertCabinetOperateLog(Integer agentId,
                                           String cabinetId,
                                           String cabinetName,
                                           String boxNum,
                                           CabinetOperateLog.OperateType operateType,
                                           CabinetOperateLog.OperatorType operatorType,
                                           String content) {
        CabinetOperateLog operateLog = new CabinetOperateLog();
        operateLog.setAgentId(agentId);
        operateLog.setCabinetId(cabinetId);
        operateLog.setCabinetName(cabinetName);
        operateLog.setBoxNum(boxNum);
        operateLog.setOperateType(operateType.getValue());
        operateLog.setOperatorType(operatorType.getValue());
        operateLog.setContent(content);
        operateLog.setCreateTime(new Date());
        cabinetOperateLogService.insert(operateLog);
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

}
