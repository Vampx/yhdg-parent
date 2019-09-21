package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReport;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetReportService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 电池上报日志
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_report")
public class CabinetReportController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(CabinetReportController.class);

    @Autowired
    CabinetReportService cabinetReportService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetReport search) {
        return PageResult.successResult(cabinetReportService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id, Date createTime) {
        CabinetReport entity = cabinetReportService.find(id, createTime);
        model.addAttribute("entity", entity);
        return "/security/hdg/cabinet_report/view";
    }

}
