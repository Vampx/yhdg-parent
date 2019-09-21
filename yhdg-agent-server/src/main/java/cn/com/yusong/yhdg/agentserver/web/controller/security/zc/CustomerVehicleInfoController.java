package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;


import cn.com.yusong.yhdg.agentserver.service.zc.CustomerVehicleInfoService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zc/customer_vehicle_info")
public class CustomerVehicleInfoController extends SecurityController {

    @Autowired
    CustomerVehicleInfoService customerVehicleInfoService;

    @SecurityControl(limits = "zc.CustomerVehicleInfo:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.CustomerVehicleInfo:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerVehicleInfo search) {
        return PageResult.successResult(customerVehicleInfoService.findPage(search));
    }


}
