package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderRefundService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;

/**
 * 换电订单
 */
@Controller
@RequestMapping(value = "/security/hdg/shop_battery_order")
public class ShopBatteryOrderController extends SecurityController {

    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    BatteryOrderRefundService batteryOrderRefundService;

//    @SecurityControl(limits = OperCodeConst.CODE_4_1_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_04.getValue());
        model.addAttribute("OrderStatusEnum", BatteryOrder.OrderStatus.values());
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("OrderStatus", BatteryOrder.OrderStatus.PAY.getValue());
        model.addAttribute("RefundStatus", BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryOrder search) {
        search.setQueryFlag(ConstEnum.Flag.TRUE.getValue());
        return PageResult.successResult(batteryOrderService.findPage(search));
    }

    @RequestMapping("page_for_balance.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageForbalance(BatteryOrder search, String balanceDate) throws ParseException {
        Date beginTime = DateUtils.parseDate(balanceDate, new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.addDays(beginTime,1);
        search.setQueryBeginTime(beginTime);
        search.setQueryEndTime(endTime);
        return PageResult.successResult(batteryOrderService.findPageForbalance(search));
    }

    @RequestMapping(value = "alipay_pay_order.htm")
    public void alipayPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPage(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(AlipayPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @RequestMapping(value = "select_battery_order.htm")
    public String selectPacketPeriodOrder(Model model,Integer agentId, Integer cabinetCompanyId, String balanceDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("cabinetCompanyId", cabinetCompanyId);
        model.addAttribute("balanceDate", balanceDate);
        return "/security/hdg/battery_order/select_battery_order";
    }

    @RequestMapping(value = "weixin_pay_order.htm")
    public void weixinPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("weixin_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult weixinPayOrderPage(WeixinPayOrder search) {
        search.setSourceType(WeixinPayOrder.SourceType.BATTERY_ORDER_CUSTOMER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
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
        return "/security/hdg/battery_order/view";
    }

    @RequestMapping(value = "advanced_query.htm")
    public void advancedQuery(Model model) {
        model.addAttribute("OrderStatusEnum", BatteryOrder.OrderStatus.values());
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
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
        return "/security/hdg/battery_order/view_basic";
    }

    @RequestMapping(value = "view_battery_order.htm")
    public String viewForegiftOrder(Model model, long customerId) {
        model.addAttribute("customerId", customerId);
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        return "/security/basic/customer/view_battery_order";
    }

    @RequestMapping(value = "edit_refund.htm")
    public String editRefund(Model model, String id) {
        BatteryOrder entity = batteryOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_order/edit_refund";
    }

    @RequestMapping("refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult refund(HttpSession httpSession, BatteryOrder batteryOrder) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        return batteryOrderRefundService.insert(userName, batteryOrder);
    }

    @RequestMapping("complete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult complete(String id) {
        return batteryOrderService.complete(id);
    }

    @RequestMapping("exchange_battery.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult exchangeBattery(String id, String batteryId) {
        return batteryOrderService.exchangeBattery(id, batteryId);
    }

    @RequestMapping("to_back_battery_order.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult toBackBatteryOrder(String id) {
        return batteryOrderService.toBackBatteryOrder(id);
    }

}
