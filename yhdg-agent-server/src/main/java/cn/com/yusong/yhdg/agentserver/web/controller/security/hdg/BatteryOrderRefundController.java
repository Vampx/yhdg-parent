package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderRefundService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 换电订单退款
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_order_refund")
public class BatteryOrderRefundController extends SecurityController {

    @Autowired
    BatteryOrderRefundService batteryOrderRefundService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;

//    @SecurityControl(limits = OperCodeConst.CODE_3_6_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_03_06_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryOrderRefund search) {
        return PageResult.successResult(batteryOrderRefundService.findPage(search));
    }

    @RequestMapping(value = "weixin_pay_order_refund.htm")
    public void weixinPayOrderRefund(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("weixin_pay_order_page_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult weixinPayOrderPageRefund(WeixinPayOrder search) {
        search.setSourceType(WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping(value = "alipay_pay_order_refund.htm")
    public void alipayPayOrderRefund(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPageRefund(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(AlipayPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        BatteryOrderRefund entity = batteryOrderRefundService.find(id);
        BatteryOrder batteryOrder = batteryOrderService.find(id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("batteryOrder", batteryOrder);
        }

        return "/security/hdg/battery_order_refund/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        BatteryOrderRefund entity = batteryOrderRefundService.find(id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("RefundStatus", BatteryOrderRefund.RefundStatus.values());
        return "/security/hdg/battery_order_refund/view_basic";
    }

}
