package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetReportDateService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_report_date")
public class CabinetReportDateController extends SecurityController {
    @Autowired
    CabinetReportDateService cabinetReportDateService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String cabinetId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        cabinetReportDateService.tree(response.getOutputStream(), cabinetId);
    }
}
