package cn.com.yusong.yhdg.webserver.web.controller.security.yms;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000008;
import cn.com.yusong.yhdg.webserver.biz.server.ClientBizUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.UpgradePackService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalCommandService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/yms/terminal_command")
public class TerminalCommandController extends SecurityController {
    @Autowired
    TerminalCommandService terminalCommandService;
    @Autowired
    UpgradePackService upgradePackService;
    @Autowired
    AppConfig appConfig;
    @Autowired
    CabinetService cabinetService;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_5_1)
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute("StatusEnum", TerminalCommand.Status.values());
        model.addAttribute("TypeEnum",TerminalCommand.Type.values());
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalCommand search) {
        return PageResult.successResult(terminalCommandService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(Long id, Model model) {
        TerminalCommand entity = terminalCommandService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("StatusEnum",TerminalCommand.Status.values());
        model.addAttribute("TypeEnum",TerminalCommand.Type.values());
        return "/security/yms/terminal_command/view";
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_restart.htm")
    public ExtResult addRestart(TerminalCommand terminalCommand, String[] ids) throws IOException {

        terminalCommand.setCreateTime(new Date());
        terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
        terminalCommand.setType(TerminalCommand.Type.RESTART.getValue());
        for(String id : ids){
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);
        }
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_shutdown.htm")
    public ExtResult addShutdown(String[] ids) throws IOException {

        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.SHUTDOWN.getValue());

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);
        }

        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_initialize.htm")
    public ExtResult addInitialize(String[] ids) throws IOException {

        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.INITIALIZATION.getValue());

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);

        }
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_upload_error_log.htm")
    public ExtResult addUploadErrorLog(String[] ids) throws IOException {

        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.UPLOAD_ERROR_LOG.getValue());

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);
        }
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_screenshot.htm")
    public ExtResult addScreenshot(String[] ids) throws IOException {
        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.SCREENSHOT.getValue());

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);

        }
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_online.htm")
    public ExtResult addOnline(String[] ids) throws IOException {

        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.ONLINE.getValue());

            Map map = new HashMap();
            map.put("version", "");
            map.put("url", "");
            map.put("memo", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);
        }
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "add_upgrade.htm")
    public ExtResult addUpgrade(String[] ids) throws IOException {

        for(String id : ids) {
            Cabinet cabinet = cabinetService.findByTerminalId(id);
            TerminalCommand terminalCommand = new TerminalCommand();
            terminalCommand.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
            terminalCommand.setCreateTime(new Date());
            terminalCommand.setStatus(TerminalCommand.Status.WAIT.getValue());
            terminalCommand.setType(TerminalCommand.Type.UPGRADE.getValue());

            UpgradePack upgradePack = upgradePackService.find(1);
            Map map = new HashMap();
            map.put("version", upgradePack.getVersion());
            map.put("url", appConfig.getStaticUrl() + upgradePack.getFilePath());
            map.put("memo", upgradePack.getMemo());
            ObjectMapper objectMapper = new ObjectMapper();
            String postBody = objectMapper.writeValueAsString(map);
            terminalCommand.setContent(postBody);

            terminalCommand.setTerminalId(id);
            terminalCommandService.create(terminalCommand);

            Msg931000008 msg = new Msg931000008();
            msg.terminalId = id;
            ClientBizUtils.noticeNewCommand(getAppConfig(), id, msg);
        }
        return ExtResult.successResult();
    }

}
