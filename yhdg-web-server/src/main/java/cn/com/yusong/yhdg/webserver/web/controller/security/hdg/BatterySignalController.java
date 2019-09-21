package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatterySignal;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.BatterySignalService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备信号记录
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_signal")
public class BatterySignalController extends SecurityController {

    @Autowired
    BatterySignalService batterySignalService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String batteryId) {
        model.addAttribute("batteryId", batteryId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatterySignal search) {
        return PageResult.successResult(batterySignalService.findPage(search));
    }

}
