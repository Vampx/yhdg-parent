package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.SwitchVehicleRecord;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.SwitchVehicleRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 换车记录
 */
@Controller
@RequestMapping(value = "/security/hdg/switch_vehicle_record")
public class SwitchVehicleRecordController extends SecurityController {
    @Autowired
    SwitchVehicleRecordService switchVehicleRecordService;

    @RequestMapping(value = "view_list_switch_record.htm")
    public void index(Model model, String orderId) {
        model.addAttribute("orderId", orderId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(SwitchVehicleRecord search) {
        return PageResult.successResult(switchVehicleRecordService.findPage(search));
    }
}
