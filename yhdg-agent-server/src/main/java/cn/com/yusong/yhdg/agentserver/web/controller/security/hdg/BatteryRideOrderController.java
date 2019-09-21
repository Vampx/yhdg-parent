package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderBatteryReportLogService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 电池骑行记录
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_ride_order")
public class BatteryRideOrderController extends SecurityController {
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    BatteryOrderBatteryReportLogService batteryOrderBatteryReportLogService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_6_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_06_02.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryOrder search) {
        return PageResult.successResult(batteryOrderService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_order.htm")
    public void selectOrder(Model model, String batteryId) {
        model.addAttribute("batteryId", batteryId);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        BatteryOrder entity = batteryOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        model.addAttribute("cabinetId", entity.getPutCabinetId());
        return "/security/hdg/battery_ride_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        BatteryOrder entity = batteryOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        if (entity.getRefundStatus() != null) {
            model.addAttribute("refundStatus", BatteryOrderRefund.RefundStatus.getName(entity.getRefundStatus()));
        }
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("OrderStatusEnum", BatteryOrder.OrderStatus.values());
        return "/security/hdg/battery_ride_order/view_basic";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_map.htm")
    public String viewMap(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/hdg/battery_ride_order/view_map";
    }

}
