package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinmpPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/weixinmp_pay_order")
public class WeixinmpPayOrderController extends SecurityController {
    @Autowired
    WeixinmpPayOrderService weixinmpPayOrderService;

    @SecurityControl(limits = "basic.WeixinmpPayOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession httpSession) {
        Integer agentId = getSessionUser(httpSession).getAgentId();
        if (agentId != 0) {
            model.addAttribute("agentId", agentId);
        }
        model.addAttribute(MENU_CODE_NAME, "basic.WeixinmpPayOrder:list");
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(WeixinmpPayOrder search) {
        return PageResult.successResult(weixinmpPayOrderService.findPage(search));
    }

    @RequestMapping("find_list.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult findList(String mobile){
        List<WeixinmpPayOrder> list = weixinmpPayOrderService.findList(mobile);

        return PageResult.successResult(list);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        WeixinmpPayOrder weixinPayOrder = weixinmpPayOrderService.find(id);
        if(weixinPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
        model.addAttribute("entity", weixinPayOrder);
        return "/security/basic/weixinmp_pay_order/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_weixinmp_pay_order.htm")
    public String viewWeixinmpPayOrder(Integer partnerId, String statsDate, Model model) {
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("statsDate", statsDate);
        return "/security/basic/partner_in_out_cash/view_weixinmp_pay_order";
    }

}
