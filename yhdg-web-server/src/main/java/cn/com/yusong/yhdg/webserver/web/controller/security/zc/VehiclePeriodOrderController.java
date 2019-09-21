package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/zc/vehicle_period_order")
public class VehiclePeriodOrderController extends SecurityController {

    @Autowired
    VehiclePeriodOrderService rentPeriodOrderService;

    @SecurityControl(limits = "zc.VehiclePeriodOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("statusList", VehiclePeriodOrder.Status.values());
        model.addAttribute(MENU_CODE_NAME, "zc.VehiclePeriodOrder:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehiclePeriodOrder search) {
        return PageResult.successResult(rentPeriodOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        VehiclePeriodOrder entity = rentPeriodOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_period_order/view";
    }
}
