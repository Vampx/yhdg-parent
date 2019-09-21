package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodPriceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/zd/rent_period_price")
public class RentPeriodPriceController extends SecurityController {
    @Autowired
    private RentBatteryTypeService rentBatteryTypeService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private RentPeriodPriceService rentPeriodPriceService;

    @RequestMapping(value = "rent_period_price.htm")
    public void rentPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
        List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findListByForegift(foregiftId, batteryType, agentId);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("rentPeriodPriceList", rentPeriodPriceList);
        model.addAttribute("foregiftId", foregiftId);
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "add.htm")
    public void add(Model model, Integer foregiftId, Integer batteryType, Integer agentId) {
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("typeName", rentBatteryType.getTypeName());
        model.addAttribute("foregiftId", foregiftId);
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(RentPeriodPrice entity) {
        return rentPeriodPriceService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        RentPeriodPrice entity = rentPeriodPriceService.find(id);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(entity.getBatteryType(), entity.getAgentId());
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Agent agent = agentService.find(entity.getAgentId());
            model.addAttribute("agentName", agent.getAgentName());
            model.addAttribute("typeName", rentBatteryType.getTypeName());
            model.addAttribute("entity", entity);
        }

        return "/security/zd/rent_period_price/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(RentPeriodPrice entity) {
        return rentPeriodPriceService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return rentPeriodPriceService.delete(id);
    }
}
