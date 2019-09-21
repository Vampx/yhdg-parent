package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinmaPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/weixinma_pay_order")
public class WeixinmaPayOrderController extends SecurityController {
    @Autowired
    WeixinmaPayOrderService weixinmaPayOrderService;

    @SecurityControl(limits = "basic.WeixinmaPayOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession httpSession) {
        Integer agentId = getSessionUser(httpSession).getAgentId();
        if (agentId != 0) {
            model.addAttribute("agentId", agentId);
        }
        model.addAttribute(MENU_CODE_NAME, "basic.WeixinmaPayOrder:list");
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(WeixinmaPayOrder search) {
        return PageResult.successResult(weixinmaPayOrderService.findPage(search));
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(String id, Model model) {
        WeixinmaPayOrder weixinPayOrder = weixinmaPayOrderService.find(id);
        if(weixinPayOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("SourceTypeEnum", PayOrder.SourceType.values());
        model.addAttribute("StatusEnum", PayOrder.Status.values());
        model.addAttribute("entity", weixinPayOrder);
        return "/security/basic/weixinma_pay_order/view";
    }

}
