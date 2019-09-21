package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 包月订单(换电订单)
 */
@Controller
@RequestMapping(value = "/security/hdg/packet_period_order")
public class PacketPeriodOrderController extends SecurityController {

    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    AgentService agentService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    CustomerInstallmentRecordOrderDetailService customerInstallmentRecordOrderDetailService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @SecurityControl(limits = "hdg.PacketPeriodOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.PacketPeriodOrder:list");
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", PacketPeriodOrder.Status.values());
        model.addAttribute("usedStatus", PacketPeriodOrder.Status.USED.getValue());
        model.addAttribute("APPLY_REFUND", PacketPeriodOrder.Status.APPLY_REFUND.getValue());
    }
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PacketPeriodOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        if (search.getStatus() == null) {
            search.setStatus(PacketPeriodOrder.Status.USED.getValue());
        }else if(search.getStatus().equals(0)){
            search.setStatus(null);
        }
        return PageResult.successResult(packetPeriodOrderService.findPage(search));
    }

    @RequestMapping("page_for_balance.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageForbalance(PacketPeriodOrder search, String balanceDate) throws ParseException {
        Date beginTime = DateUtils.parseDate(balanceDate, new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.addDays(beginTime,1);
        search.setQueryBeginTime(beginTime);
        search.setQueryEndTime(endTime);
        return PageResult.successResult(packetPeriodOrderService.findPageForbalance(search));
    }

    @RequestMapping("page_for_clearing.htm")
    @ResponseBody
    public Map pageForClearing(PacketPeriodOrder search) {
        return packetPeriodOrderService.findPageForClearing(search);
    }

    @RequestMapping(value = "alipay_pay_order.htm")
    public void alipayPayOrder(Model model,String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPage(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
        search.setOrderStatus(AlipayPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(alipayPayOrderService.findPage(search));
    }

    @RequestMapping(value = "weixin_pay_order.htm")
    public void weixinPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("weixin_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult weixinPayOrderPage(WeixinPayOrder search) {
        search.setSourceType(WeixinPayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping("exchange_installment.htm")
    public void exchangeInstallment(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("exchange_installment_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult exchangeInstallmentPage(CustomerInstallmentRecordPayDetail search) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = customerInstallmentRecordOrderDetailService.findOrderBySourceId(search.getSourceId(), ConstEnum.Category.EXCHANGE.getValue());
        if (customerInstallmentRecordOrderDetail != null) {
            search.setRecordId(customerInstallmentRecordOrderDetail.getRecordId());
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }else{
            search.setRecordId(-1L);
            return PageResult.successResult(customerInstallmentRecordPayDetailService.findPage(search));
        }
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        PacketPeriodOrder entity = packetPeriodOrderService.find(id);

        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/hdg/packet_period_order/view";
    }

    @RequestMapping(value = "edit_extend_rent.htm")
    public String extendRent(Model model, String id) {
        PacketPeriodOrder entity = packetPeriodOrderService.find(id);

        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/hdg/packet_period_order/edit_extend_rent";
    }

    @RequestMapping("extend_rent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult extendRent(PacketPeriodOrder entity, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return packetPeriodOrderService.extendRent(entity, sessionUser.getUsername());
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        PacketPeriodOrder entity = packetPeriodOrderService.find(id);

        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", PacketPeriodOrder.Status.values());
        return "/security/hdg/packet_period_order/view_basic";
    }

    @RequestMapping(value = "select_packet_period_order.htm")
    public String selectPacketPeriodOrder(Model model,Integer agentId, String balanceDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("balanceDate", balanceDate);
        return "/security/hdg/packet_period_order/select_packet_period_order";
    }

    @RequestMapping(value = "select_refund_packet_period_order.htm")
    public String selectPacketPeriodOrder(Model model,String orderRefundId) {
        model.addAttribute("orderRefundId", orderRefundId);
        return "/security/hdg/packet_period_order/select_refund_packet_period_order";
    }

    @ResponseBody
    @RequestMapping("find_period_order.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findPeriodOrder(String id) {
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(id);
        if (packetPeriodOrder == null) {
            return DataResult.failResult("请确认该订单是否存在！");
        } else {
            return DataResult.successResult(packetPeriodOrder);
        }
    }

    @RequestMapping(value = "edit_refund.htm")
    public String editRefund(Model model, String id, int refundFlag) {
        PacketPeriodOrder entity = packetPeriodOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            if (entity.getRefundMoney() == null) {
                entity.setRefundMoney(0);
            }
            model.addAttribute("refundFlag", refundFlag);
            model.addAttribute("entity", entity);
        }

        return "/security/hdg/packet_period_order/edit_refund";
    }

    @RequestMapping(value = "edit_repulse_refund.htm")
    public String editRepulseRefund(Model model, String id) {
        PacketPeriodOrder entity = packetPeriodOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/packet_period_order/edit_repulse_refund";
    }

    @RequestMapping("refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult refund(HttpSession httpSession, PacketPeriodOrder entity) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        return packetPeriodOrderService.refund(userName, entity);
    }

    @RequestMapping("repulse_refund.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult repulseRefund(HttpSession httpSession, PacketPeriodOrder entity) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        return packetPeriodOrderService.repulseRefund(userName, entity);
    }
}
