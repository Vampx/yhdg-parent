package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordFault;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryChargeRecordFaultService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  充电订单故障
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_charge_record_fault")
public class BatteryChargeRecordFaultController extends SecurityController {
    @Autowired
    BatteryChargeRecordFaultService batteryChargeRecordFaultService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryChargeRecordFault search) {
        return PageResult.successResult(batteryChargeRecordFaultService.findPage(search));
    }
}
