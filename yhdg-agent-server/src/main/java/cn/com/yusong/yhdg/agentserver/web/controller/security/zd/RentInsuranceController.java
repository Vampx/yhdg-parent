package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentInsuranceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/zd/rent_insurance")
public class RentInsuranceController extends SecurityController {
    @Autowired
    private RentInsuranceService rentInsuranceService;
    @Autowired
    private RentBatteryTypeService rentBatteryTypeService;
    @Autowired
    private AgentService agentService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentInsurance search) {
        return PageResult.successResult(rentInsuranceService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(RentInsurance entity) {
        return rentInsuranceService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        RentInsurance entity = rentInsuranceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_insurance/edit";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        RentInsurance entity = rentInsuranceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_insurance/view";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(RentInsurance entity) {
        return rentInsuranceService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return rentInsuranceService.delete(id);
    }

    @RequestMapping(value = "rent_insurance.htm")
    public void exchangeBatteryTypeInsurance(Model model, Integer batteryType, Integer agentId) {
        List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("rentInsuranceList", rentInsuranceList);
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "rent_insurance_add.htm")
    public void exchangeBatteryTypeInsuranceAdd(Model model, Integer batteryType, Integer agentId) {
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(batteryType, agentId);
        model.addAttribute("entity", rentBatteryType);
        model.addAttribute("typeName", rentBatteryType.getTypeName());
        model.addAttribute("batteryType", batteryType);
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping(value = "rent_insurance_edit.htm")
    public String exchangeBatteryTypeInsuranceEdit(Model model, Long id) {
        RentInsurance entity = rentInsuranceService.find(id);
        RentBatteryType rentBatteryType = rentBatteryTypeService.findByBatteryTypeAndAgent(entity.getBatteryType(), entity.getAgentId());
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Agent agent = agentService.find(entity.getAgentId());
            model.addAttribute("agentName", agent.getAgentName());
            model.addAttribute("typeName", rentBatteryType.getTypeName());
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_insurance/rent_insurance_edit";
    }
}
