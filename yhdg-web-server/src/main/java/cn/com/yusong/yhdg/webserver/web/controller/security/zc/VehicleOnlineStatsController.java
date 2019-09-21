package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOnlineStats;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleOnlineStatsService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zc/vehicle_online_stats")
public class VehicleOnlineStatsController extends SecurityController {
    @Autowired
    VehicleOnlineStatsService vehicleOnlineStatsService;

    @RequestMapping(value = "index.htm")
    public void index(Integer vehicleId, Model model) {
        model.addAttribute("vehicleId", vehicleId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleOnlineStats search) {
        return PageResult.successResult(vehicleOnlineStatsService.findPage(search));
    }

}
