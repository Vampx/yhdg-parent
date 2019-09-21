package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBattery;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetReportBatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 电池上报日志
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_report_battery")
public class CabinetReportBatteryController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(CabinetReportBatteryController.class);

    @Autowired
    CabinetReportBatteryService cabinetReportBatteryService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetReportBattery search) {
        return PageResult.successResult(cabinetReportBatteryService.findPage(search));
    }
}
