package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;

/**
 * 包月订单(换电订单)
 */
@Controller
@RequestMapping(value = "/security/hdg/shop_packet_period_order")
public class ShopPacketPeriodOrderController extends SecurityController {

    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    AgentService agentService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;

//    @SecurityControl(limits = OperCodeConst.CODE_4_1_6_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_06.getValue());
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", PacketPeriodOrder.Status.values());
        model.addAttribute("usedStatus", PacketPeriodOrder.Status.USED.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PacketPeriodOrder search) {
//        if (search.getStatus() == null) {
//            search.setStatus(PacketPeriodOrder.Status.USED.getValue());
//        }else if(search.getStatus().equals(0)){
//            search.setStatus(null);
//        }
        search.setQueryFlag(ConstEnum.Flag.TRUE.getValue());
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
    public String selectPacketPeriodOrder(Model model,Integer agentId, Integer cabinetCompanyId, String balanceDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("cabinetCompanyId", cabinetCompanyId);
        model.addAttribute("balanceDate", balanceDate);
        return "/security/hdg/packet_period_order/select_packet_period_order";
    }

}
