package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryForegiftService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/zd/rent_battery_foregift")
public class RentBatteryForegiftController extends SecurityController {
    @Autowired
    private RentBatteryForegiftService rentBatteryForegiftService;
    @Autowired
    private RentBatteryTypeService rentBatteryTypeService;
    @Autowired
    private AgentService agentService;

    @RequestMapping(value = "find_foregift_list.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult findForegiftList(Integer batteryType, Integer agentId) {
        List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
        if (rentBatteryForegiftList != null && rentBatteryForegiftList.size() > 0) {
            return DataResult.successResult(rentBatteryForegiftList.get(0));
        } else {
            return ExtResult.failResult("无押金信息");
        }
    }

    @RequestMapping(value = "rent_battery_foregift.htm")
    public void rentBatteryForegfit(Model model, Integer batteryType, Integer agentId) {
        List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("rentBatteryForegiftList", rentBatteryForegiftList);
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add.htm")
    public void add(Model model, Integer batteryType, Integer agentId) {
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("typeName", rentBatteryType.getTypeName());
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(RentBatteryForegift entity) {
        return rentBatteryForegiftService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        RentBatteryForegift entity = rentBatteryForegiftService.find(id);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(entity.getBatteryType(), entity.getAgentId());
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Agent agent = agentService.find(entity.getAgentId());
            model.addAttribute("agentName", agent.getAgentName());
            model.addAttribute("typeName", rentBatteryType.getTypeName());
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_battery_foregift/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(RentBatteryForegift entity) {
        return rentBatteryForegiftService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return rentBatteryForegiftService.delete(id);
    }
}
