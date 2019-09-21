package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodActivityService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/packet_period_activity")
public class PacketPeriodActivityController extends SecurityController {

    @Autowired
    PacketPeriodActivityService activityService;
    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;

    @SecurityControl(limits = "hdg.PacketPeriodActivity:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.PacketPeriodActivity:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PacketPeriodActivity search) {
        return PageResult.successResult(activityService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id, Integer editFlag) {
        model.addAttribute("editFlag", editFlag);
        PacketPeriodActivity entity = activityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/packet_period_activity/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, long id) {
        PacketPeriodActivity entity = activityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/packet_period_activity/edit_basic";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(PacketPeriodActivity activity) {
        return activityService.create(activity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(PacketPeriodActivity activity) {
        return activityService.update(activity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return activityService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        PacketPeriodActivity entity = activityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/packet_period_activity/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        PacketPeriodActivity entity = activityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/packet_period_activity/view_basic";
    }
}
