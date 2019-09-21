package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryStatis;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/security/hdg/battery_factory_control")
public class BatteryFactoryControlController extends SecurityController {

    @Autowired
    BatteryService batteryService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_8_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer agentId, String id, Integer monitorStatus) {
        model.addAttribute("agentId", agentId);
        if (agentId == null) {
            agentId = 0;
        }
        if (monitorStatus == null) {
            monitorStatus = 0;
        }
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_08_04.getValue());
        model.addAttribute("list", batteryService.findByAgent(agentId, id, monitorStatus));
    }

    @RequestMapping(value = "battery_detail.htm")
    public void batteryDetail(Model model, Integer agentId, String id, Integer monitorStatus) {
        model.addAttribute("agentId", agentId);
        if (agentId == null) {
            agentId = 0;
        }
        if (monitorStatus == null) {
            monitorStatus = 0;
        }
        model.addAttribute("list", batteryService.findByAgent(agentId, id, monitorStatus));
    }

}
