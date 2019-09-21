package cn.com.yusong.yhdg.webserver.web.controller.security.zc;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleForegiftOrderService;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zc/vehicle_foregift_order")
public class VehicleForegiftOrderController extends SecurityController {

    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;

    @SecurityControl(limits = "zc.VehicleForegiftOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("statusList", VehicleForegiftOrder.Status.values());
        model.addAttribute(MENU_CODE_NAME, "zc.VehicleForegiftOrder:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleForegiftOrder search) {
        return PageResult.successResult(vehicleForegiftOrderService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        VehicleForegiftOrder entity = vehicleForegiftOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_foregift_order/view";
    }

}
