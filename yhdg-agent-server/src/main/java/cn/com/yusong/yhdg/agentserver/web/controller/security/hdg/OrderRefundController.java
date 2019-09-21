package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.OrderRefund;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.OrderRefundService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;

/**
 * 订单退款(包月退款和换电订单退款联合查询，待后续退款表合并)
 */
@Controller
@RequestMapping(value = "/security/hdg/order_refund")
public class OrderRefundController extends SecurityController {

    @Autowired
    OrderRefundService orderRefundService;

    @RequestMapping(value = "select_order_refund.htm")
    public String selectOrderRefund(Model model, Integer agentId, String balanceDate) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("balanceDate", balanceDate);
        model.addAttribute("typeEnum", OrderRefund.Type.values());
        return "/security/hdg/order_refund/select_order_refund";
    }

    @RequestMapping("page_for_balance.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageForBalance(OrderRefund search, String balanceDate) throws ParseException {
        Date beginTime = DateUtils.parseDate(balanceDate, new String[]{"yyyy-MM-dd"});
        Date endTime = DateUtils.addDays(beginTime,1);
        search.setStatus(BatteryOrderRefund.RefundStatus.REFUND_SUCCESS.getValue());
        search.setQueryBeginTime(beginTime);
        search.setQueryEndTime(endTime);
        return PageResult.successResult(orderRefundService.findPageForBalance(search));
    }
}
