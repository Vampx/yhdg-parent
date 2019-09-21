package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.BespeakOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  退租电池订单
 */
@Controller
@RequestMapping(value = "/security/hdg/bespeak_order")
public class BespeakOrderController extends SecurityController {

    @Autowired
    BespeakOrderService bespeakOrderService;

    @SecurityControl(limits = "hdg.BespeakOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.BespeakOrder:list");
        model.addAttribute("StatusEnum", BespeakOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BespeakOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.FALSE.getValue());
        return PageResult.successResult(bespeakOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/hdg/bespeak_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        BespeakOrder entity = bespeakOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("StatusEnum", BespeakOrder.Status.values());
        return "/security/hdg/bespeak_order/view_basic";
    }


    @RequestMapping("complete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult complete(String id) {
        return bespeakOrderService.complete(id);
    }
}
