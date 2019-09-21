package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zc/vehicle_order")
public class VehicleOrderConrtroller extends SecurityController {

    @Autowired
    VehicleOrderService vehicleOrderService;

    @SecurityControl(limits = "zc.VehicleOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("statusList",VehicleOrder.Status.values());
        model.addAttribute(MENU_CODE_NAME, "zc.VehicleOrder:list");
    }
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleOrder search) {

        return PageResult.successResult(vehicleOrderService.findPage(search));
    }
    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        VehicleOrder entity = vehicleOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_order/view";
    }


}
