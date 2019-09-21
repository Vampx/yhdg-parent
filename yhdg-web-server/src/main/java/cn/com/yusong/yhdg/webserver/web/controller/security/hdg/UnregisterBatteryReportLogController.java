package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.UnregisterBatteryReportLogService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(value = "/security/hdg/unregister_battery_report_log")
public class UnregisterBatteryReportLogController extends SecurityController {
    @Autowired
    UnregisterBatteryReportLogService unregisterBatteryReportLogService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_6_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_06_04.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(UnregisterBatteryReportLog search) {
        return PageResult.successResult(unregisterBatteryReportLogService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String code, Date createTime) {
        UnregisterBatteryReportLog entity = unregisterBatteryReportLogService.find(code, createTime);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/unregister_battery_report_log/view";
    }

}
