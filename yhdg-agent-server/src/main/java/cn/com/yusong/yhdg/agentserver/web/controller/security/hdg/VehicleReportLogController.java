package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VehicleReportLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.VehicleReportLogService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 车辆上报日志
 */
@Controller
@RequestMapping(value = "/security/hdg/vehicle_report_log")
public class VehicleReportLogController extends SecurityController {

    @Autowired
    VehicleReportLogService vehicleReportLogService;


//    @SecurityControl(limits = OperCodeConst.CODE_2_5_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_05_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleReportLog search) {
        return PageResult.successResult(vehicleReportLogService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String batteryId, Date reportTime) {
        VehicleReportLog entity = vehicleReportLogService.find(batteryId, reportTime);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/vehicle_report_log/view";
    }

}
