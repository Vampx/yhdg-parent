package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.KeepOrder;
import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.KeepTakeOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 收电订单
 */
@Controller
@RequestMapping(value = "/security/hdg/keep_take_order")
public class KeepTakeOrderController extends SecurityController {
    @Autowired
    KeepTakeOrderService keepTakeOrderService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_7_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_07_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(KeepTakeOrder search) {
        return PageResult.successResult(keepTakeOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        KeepTakeOrder entity = keepTakeOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/hdg/keep_take_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        KeepTakeOrder entity = keepTakeOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/keep_take_order/view_basic";
    }

    @RequestMapping(value = "view_keep_order_list.htm")
    public void viewKeepOrderList(Model model, String id) {
        model.addAttribute("takeOrderId", id);
        model.addAttribute("OrderStatusEnum", KeepOrder.OrderStatus.values());
    }



}
