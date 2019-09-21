package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/battery_monitor")
public class BatteryMonitorController extends SecurityController {
    @Autowired
    BatteryService batteryService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_8_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer agentId) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_08_05.getValue());
        int batteryTotal = batteryService.findCountByPositionState(null, null, agentId);
        int motionTotal = batteryService.findCountByPositionState(Battery.PositionState.MOVE.getValue(), ConstEnum.Flag.TRUE.getValue(), agentId);
        int notOnlineTotal = batteryService.findCountByPositionState(null, ConstEnum.Flag.FALSE.getValue(), agentId);
        int staticTotal = batteryTotal-motionTotal-notOnlineTotal;
        model.addAttribute("batteryTotal", batteryTotal);
        model.addAttribute("motionTotal", motionTotal);
        model.addAttribute("staticTotal", staticTotal);
        model.addAttribute("notOnlineTotal", notOnlineTotal);
        model.addAttribute("agentId", agentId);
    }

//    @RequestMapping("query.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public DataResult query(String id, Integer agentId) throws IOException {
//        return batteryService.findList(id, agentId);
//    }

    @RequestMapping("query.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult query(Battery search) {
        return PageResult.successResult(batteryService.findList(search));
    }

}
