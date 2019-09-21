package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
