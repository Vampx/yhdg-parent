package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.KeepOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 维护订单
 */
@Controller
@RequestMapping(value = "/security/hdg/keep_order")
public class KeepOrderController extends SecurityController {

    @Autowired
    KeepOrderService keepOrderService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_7_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("StatusEnum", KeepOrder.OrderStatus.values());
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_07_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(KeepOrder search) {
        return PageResult.successResult(keepOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        KeepOrder entity = keepOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/hdg/keep_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        KeepOrder entity = keepOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("OrderStatusEnum", KeepOrder.OrderStatus.values());
        return "/security/hdg/keep_order/view_basic";
    }

}
