package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryParameterLogService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 电池参数修改记录
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_parameter_log")
public class BatteryParameterLogController extends SecurityController {

    @Autowired
    BatteryParameterLogService batteryParameterLogService;


    @RequestMapping(value = "index.htm")
    public String parameter(Model model, String id) {
        model.addAttribute("batteryId", id);
        model.addAttribute("StatusEnum", BatteryParameterLog.Status.values());
        return "/security/hdg/battery_parameter_log/index";
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryParameterLog search) {
        return PageResult.successResult(batteryParameterLogService.findPage(search));
    }

    @RequestMapping("find.htm")
    public void find(Integer id, Model model) {
        model.addAttribute("entity", batteryParameterLogService.find(id));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Integer id) {
        BatteryParameterLog entity = batteryParameterLogService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);;
            model.addAttribute("StatusEnum", BatteryParameterLog.Status.values());
        }
        return "/security/hdg/battery_parameter_log/view";
    }
}
