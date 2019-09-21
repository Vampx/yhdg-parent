package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordOrderDetailService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordOrderDetail;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;

@Controller
@RequestMapping(value = "/security/zd/rent_period_order")
public class RentPeriodOrderController extends SecurityController {
    @Autowired
    private RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    private AlipayPayOrderService alipayPayOrderService;
    @Autowired
    private WeixinPayOrderService weixinPayOrderService;
    @Autowired
    CustomerInstallmentRecordOrderDetailService customerInstallmentRecordOrderDetailService;
    @Autowired
    CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

    @SecurityControl(limits = "zd.RentPeriodOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.RentPeriodOrder:list");
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", RentPeriodOrder.Status.values());
        model.addAttribute("usedStatus", RentPeriodOrder.Status.USED.getValue());
        model.addAttribute("APPLY_REFUND", RentPeriodOrder.Status.APPLY_REFUND.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentPeriodOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        if (search.getStatus() == null) {
            search.setStatus(RentPeriodOrder.Status.USED.getValue());
        } else if (search.getStatus().equals(0)) {
            search.setStatus(null);
        }
        return PageResult.successResult(rentPeriodOrderService.findPage(search));
    }

    @RequestMapping("page_for_balance.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageForbalance(RentPeriodOrder search, String balanceDate) throws ParseException {
        Date beginTime = DateUtils.parseDate(balanceDate, new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.addDays(beginTime, 1);
        search.setQueryBeginTime(beginTime);
        search.setQueryEndTime(endTime);
        return PageResult.successResult(rentPeriodOrderService.findPageForbalance(search));
    }

    @RequestMapping(value = "alipay_pay_order.htm")
    public void alipayPayOrder(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("alipay_pay_order_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult alipayPayOrderPage(AlipayPayOrder search) {
        search.setSourceType(AlipayPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
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
        search.setSourceType(WeixinPayOrder.SourceType.RENT_PERIOD_ORDER_PAY.getValue());
        search.setOrderStatus(WeixinPayOrder.Status.SUCCESS.getValue());
        return PageResult.successResult(weixinPayOrderService.findPage(search));
    }

    @RequestMapping("rent_installment.htm")
    public void rentInstallment(Model model, String id) {
        model.addAttribute("sourceId", id);
    }

    @RequestMapping("rent_installment_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult rentInstallmentPage(CustomerInstallmentRecordPayDetail search) {
        CustomerInstallmentRecordOrderDetail customerInstallmentRecordOrderDetail = customerInstallmentRecordOrderDetailService.findOrderBySourceId(search.getSourceId(), ConstEnum.Category.RENT.getValue());
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
        RentPeriodOrder entity = rentPeriodOrderService.find(id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/zd/rent_period_order/view";
    }

    @RequestMapping(value = "edit_extend_rent.htm")
    public String extendRent(Model model, String id) {
        RentPeriodOrder entity = rentPeriodOrderService.find(id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/zd/rent_period_order/edit_extend_rent";
    }

    @RequestMapping("extend_rent.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult extendRent(RentPeriodOrder entity, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        return rentPeriodOrderService.extendRent(entity, sessionUser.getUsername());
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        RentPeriodOrder entity = rentPeriodOrderService.find(id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", RentPeriodOrder.Status.values());
        return "/security/zd/rent_period_order/view_basic";
    }

    @RequestMapping(value = "select_rent_period_order.htm")
    public String selectRentPeriodOrder(Model model, Integer agentId, String balanceDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("balanceDate", balanceDate);
        return "/security/hdg/packet_period_order/select_packet_period_order";
    }

    @RequestMapping(value = "select_refund_rent_period_order.htm")
    public String selectRentPeriodOrder(Model model, String orderRefundId) {
        model.addAttribute("orderRefundId", orderRefundId);
        return "/security/zd/rent_period_order/select_refund_rent_period_order";
    }

    @ResponseBody
    @RequestMapping("find_period_order.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findPeriodOrder(String id) {
        RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(id);
        if (rentPeriodOrder == null) {
            return DataResult.failResult("请确认该订单是否存在！");
        } else {
            return DataResult.successResult(rentPeriodOrder);
        }
    }
}
