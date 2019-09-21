package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.BackBatteryOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  退租电池订单
 */
@Controller
@RequestMapping(value = "/security/hdg/back_battery_order")
public class BackBatteryOrderController extends SecurityController {

    @Autowired
    BackBatteryOrderService backBatteryOrderService;

    @SecurityControl(limits = "hdg.BackBatteryOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.BackBatteryOrder:list");
        model.addAttribute("OrderStatusEnum", BackBatteryOrder.OrderStatus.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BackBatteryOrder search) {
        return PageResult.successResult(backBatteryOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/hdg/back_battery_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        BackBatteryOrder entity = backBatteryOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("OrderStatusEnum", BackBatteryOrder.OrderStatus.values());
        return "/security/hdg/back_battery_order/view_basic";
    }

    @RequestMapping(value = "view_battery_order.htm")
    public String viewForegiftOrder(Model model, long customerId) {
        model.addAttribute("customerId", customerId);
        return "/security/basic/customer/view_battery_order";
    }

}
