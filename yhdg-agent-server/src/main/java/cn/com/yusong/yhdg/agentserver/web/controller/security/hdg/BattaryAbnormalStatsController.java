package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.BattaryAbnormalStatsService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
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
@RequestMapping("/security/hdg/battary_abnormal_stats")
public class BattaryAbnormalStatsController extends SecurityController {

    @Autowired
    BatteryService batteryService;
    @Autowired
    BattaryAbnormalStatsService battaryAbnormalStatsService;
    @SecurityControl(limits = "hdg.BattaryAbnormalStats:list")
    @RequestMapping("index.htm")
    public void index(Model model){
        model.addAttribute(MENU_CODE_NAME, "hdg.BattaryAbnormalStats:list");
    }


    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Battery battery, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        if(battery.getAgentId()==null){
            battery.setAgentId(sessionUser.getAgentId());
        }
        battery.setCategory(Battery.Category.EXCHANGE.getValue());
        return PageResult.successResult(batteryService.findByAbnormalAllPage(battery));
    }

    @RequestMapping("export_excel.htm")
    public void exportExcel(Integer agentId, String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"异常电池总统计.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        Battery box = new Battery();
        box.setId(id);
        box.setAgentId(agentId);
        box.setCategory(Battery.Category.EXCHANGE.getValue());
        StatsXlsUtils.writeBattaryAbnormal(battaryAbnormalStatsService.findList(box), os);
        downloadSupport(writeFile, request, response, "异常电池总统计.xls");
    }



}
