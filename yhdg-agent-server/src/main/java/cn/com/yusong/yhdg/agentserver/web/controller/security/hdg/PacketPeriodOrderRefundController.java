package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayfwPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinmpPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 包月订单退款(换电订单退款)
 */
@Controller
@RequestMapping(value = "/security/hdg/packet_period_order_refund")
public class PacketPeriodOrderRefundController extends SecurityController {

    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    AlipayfwPayOrderService alipayfwPayOrderService;
    @Autowired
    WeixinmpPayOrderService weixinmpPayOrderService;

////    @SecurityControl(limits = OperCodeConst.CODE_3_6_4_1)
//    @RequestMapping(value = "index.htm")
//    public void index(Model model) {
////        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_03_06_04.getValue());
//        model.addAttribute("RefundStatus", PacketPeriodOrderRefund.RefundStatus.values());
//        model.addAttribute("APPLY_REFUND", PacketPeriodOrderRefund.RefundStatus.APPLY_REFUND.getValue());
//    }
//
//    @RequestMapping("page.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult page(PacketPeriodOrderRefund search) {
//        return PageResult.successResult(packetPeriodOrderRefundService.findPage(search));
//    }
//
//    @RequestMapping(value = "weixin_pay_order_refund.htm")
//    public void weixinPayOrderRefund(Model model, String id) {
//        model.addAttribute("orderRefundId", id);
//    }
//
//    @RequestMapping("weixin_pay_order_page_refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult weixinPayOrderPageRefund(WeixinPayOrder search) {
//        search.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
//        search.setOrderStatus(PayOrder.Status.SUCCESS.getValue());
//        return PageResult.successResult(weixinPayOrderService.findPageByPacketRefund(search));
//    }
//
//    @RequestMapping(value = "alipay_pay_order_refund.htm")
//    public void alipayPayOrderRefund(Model model, String id) {
//        model.addAttribute("orderRefundId", id);
//    }
//
//    @RequestMapping("alipay_pay_order_page_refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult alipayPayOrderPageRefund(AlipayPayOrder search) {
//        search.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
//        search.setOrderStatus(PayOrder.Status.SUCCESS.getValue());
//        return PageResult.successResult(alipayPayOrderService.findPageByPacketRefund(search));
//    }
//
//    @RequestMapping(value = "weixinmp_pay_order_refund.htm")
//    public void weixinmpPayOrderRefund(Model model, String id) {
//        model.addAttribute("orderRefundId", id);
//    }
//
//    @RequestMapping("weixinmp_pay_order_page_refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult weixinmpPayOrderPageRefund(WeixinmpPayOrder search) {
//        search.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
//        search.setOrderStatus(PayOrder.Status.SUCCESS.getValue());
//        return PageResult.successResult(weixinmpPayOrderService.findPageByPacketRefund(search));
//    }
//
//    @RequestMapping(value = "alipayfw_pay_order_refund.htm")
//    public void alipayfwPayOrderRefund(Model model, String id) {
//        model.addAttribute("orderRefundId", id);
//    }
//
//    @RequestMapping("alipayfw_pay_order_page_refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult alipayfwPayOrderPageRefund(AlipayfwPayOrder search) {
//        search.setSourceType(PayOrder.SourceType.PACKET_PERIOD_ORDER_PAY.getValue());
//        search.setOrderStatus(PayOrder.Status.SUCCESS.getValue());
//        return PageResult.successResult(alipayfwPayOrderService.findPageByPacketRefund(search));
//    }
//
//    @RequestMapping(value = "view.htm")
//    public String view(Model model, String id) {
//        PacketPeriodOrderRefund entity = packetPeriodOrderRefundService.find(id);
//        if(entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//
//        return "/security/hdg/packet_period_order_refund/view";
//    }
//
//    @RequestMapping(value = "view_basic.htm")
//    public String viewBasic(Model model, String id) {
//        PacketPeriodOrderRefund entity = packetPeriodOrderRefundService.find(id);
//        if(entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        model.addAttribute("RefundStatus", PacketPeriodOrderRefund.RefundStatus.values());
//        return "/security/hdg/packet_period_order_refund/view_basic";
//    }
//
//    @RequestMapping(value = "edit_refund.htm")
//    public String editRefund(Model model,  String id) {
//        PacketPeriodOrderRefund entity = packetPeriodOrderRefundService.find(id);
//        if(entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        return "/security/hdg/packet_period_order_refund/edit_refund";
//    }
//
//    @RequestMapping("refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public ExtResult refund(HttpSession httpSession, PacketPeriodOrderRefund packetPeriodOrderRefund) {
//        SessionUser sessionUser = getSessionUser(httpSession);
//        String userName = sessionUser.getUsername();
//        return  packetPeriodOrderRefundService.refund(userName, packetPeriodOrderRefund);
//    }
//
//    /**
//     * 拒绝退款
//     * @param model
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "edit_repulse_refund.htm")
//    public String editRepulseRefund(Model model, String id) {
//        PacketPeriodOrderRefund entity = packetPeriodOrderRefundService.find(id);
//        if(entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        return "/security/hdg/packet_period_order_refund/edit_repulse_refund";
//    }
//
//    @RequestMapping("repulse_refund.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public ExtResult repulseRefund(HttpSession httpSession, PacketPeriodOrderRefund packetPeriodOrderRefund) {
//        SessionUser sessionUser = getSessionUser(httpSession);
//        String userName = sessionUser.getUsername();
//        return packetPeriodOrderRefundService.repulseRefund(userName, packetPeriodOrderRefund);
//    }
//
//    @RequestMapping(value = "list.htm")
//    public void list(Model model, String id) {
//        model.addAttribute("id", id);
//    }
//

}
