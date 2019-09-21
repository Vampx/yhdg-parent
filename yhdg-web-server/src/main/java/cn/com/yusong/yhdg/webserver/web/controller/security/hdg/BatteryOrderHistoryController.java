package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderHistoryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 换电订单
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_order_history")
public class BatteryOrderHistoryController extends SecurityController {

    @Autowired
    BatteryOrderHistoryService batteryOrderHistoryService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        batteryOrderHistoryService.tree(response.getOutputStream());
    }

//    @SecurityControl(limits = OperCodeConst.CODE_2_4_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_04_04.getValue());
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
    }

    @RequestMapping(value = "advanced_query.htm")
    public void advancedQuery(Model model) {
        model.addAttribute("OrderStatusEnum", BatteryOrder.OrderStatus.values());
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryOrder search) {
        return PageResult.successResult(batteryOrderHistoryService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id,String suffix) {
        BatteryOrder entity = batteryOrderHistoryService.find(id,suffix);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        model.addAttribute("suffix", suffix);

        return "/security/hdg/battery_order_history/view";
    }
    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id,String suffix) {
        BatteryOrder entity = batteryOrderHistoryService.find(id,suffix);
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
        return "/security/hdg/battery_order_history/view_basic";
    }

}
