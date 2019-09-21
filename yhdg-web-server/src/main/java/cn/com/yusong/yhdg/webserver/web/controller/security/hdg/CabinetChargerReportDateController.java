package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryReportDateService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetChargerReportDateService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_charger_report_date")
public class CabinetChargerReportDateController extends SecurityController {
    @Autowired
    CabinetChargerReportDateService cabinetChargerReportDateService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String cabinetId, String boxNum, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        cabinetChargerReportDateService.tree(response.getOutputStream(), cabinetId, boxNum);
    }
}
