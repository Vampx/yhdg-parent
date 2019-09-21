package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetBoxProhibitStatsService;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetBoxStatsService;
import cn.com.yusong.yhdg.agentserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


@Controller
@RequestMapping("/security/hdg/cabinet_box_prohibit_stats")
public class CabinetBoxProhibitStatsController extends SecurityController {


    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetBoxStatsService cabinetBoxStatsService;
    @Autowired
    CabinetBoxProhibitStatsService cabinetBoxProhibitStatsService;
    @SecurityControl(limits = "hdg.CabinetBoxProhibitStats:list")
    @RequestMapping("index.htm")
    public void index(Model model){
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetBoxProhibitStats:list");
    }


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetBox cabinetBox, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        if(cabinetBox.getAgentId()==null){
            cabinetBox.setAgentId(sessionUser.getAgentId());
        }
        return PageResult.successResult(cabinetBoxService.findByIsActiveAllPage(cabinetBox));
    }
    @RequestMapping("view.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public String view(String boxNum,String cabinetId, Model model){
        CabinetBox byId = cabinetBoxService.findById(cabinetId,boxNum);
        if (byId == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity",byId);
        return "/security/hdg/cabinet_box_prohibit_stats/view";
    }
    @RequestMapping("export_excel.htm")
    public void exportExcel(Integer agentId, String cabinetId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"禁用箱门总统计.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        CabinetBox box = new CabinetBox();
        box.setAgentId(agentId);
        box.setCabinetId(cabinetId);
        StatsXlsUtils.writeCabinetMouth(cabinetBoxProhibitStatsService.findList(box), os);
        downloadSupport(writeFile, request, response, "禁用箱门总统计.xls");
    }

}
