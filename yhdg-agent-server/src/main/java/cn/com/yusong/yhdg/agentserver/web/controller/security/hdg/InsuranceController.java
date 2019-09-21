package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.InsuranceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 电池类型保险
 */
@Controller
@RequestMapping(value = "/security/hdg/insurance")
public class InsuranceController extends SecurityController {
	@Autowired
	InsuranceService insuranceService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;
	@Autowired
	AgentService agentService;

//	@SecurityControl(limits = OperCodeConst.CODE_3_7_1_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_03_07_01.getValue());
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(Insurance search) {
		return PageResult.successResult(insuranceService.findPage(search));
	}

	@RequestMapping(value = "add.htm")
	public void exchangeBatteryTypeInsuranceAdd(Model model) {
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(Insurance entity) {
		return insuranceService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		Insurance entity = insuranceService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/insurance/edit";
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		Insurance entity = insuranceService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/insurance/view";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(Insurance entity) {
		return insuranceService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id) {
		return insuranceService.delete(id);
	}

	@RequestMapping(value = "exchange_insurance.htm")
	public void exchangeBatteryTypeInsurance(Model model, Integer batteryType, Integer agentId) {
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("insuranceList", insuranceList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "exchange_insurance_add.htm")
	public void exchangeBatteryTypeInsuranceAdd(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("typeName", agentBatteryType.getTypeName());
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("exchange_insurance_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryTypeInsuranceCreate(Insurance entity) {
		return insuranceService.create(entity);
	}

	@RequestMapping(value = "exchange_insurance_edit.htm")
	public String exchangeBatteryTypeInsuranceEdit(Model model, Long insuranceId) {
		Insurance entity = insuranceService.find(insuranceId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(entity.getBatteryType(), entity.getAgentId());
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("typeName", agentBatteryType.getTypeName());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/insurance/exchange_insurance_edit";
	}

	@RequestMapping("exchange_insurance_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryTypeInsuranceUpdate(Insurance entity) {
		return insuranceService.update(entity);
	}

	@RequestMapping("exchange_insurance_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryTypeInsuranceDelete(Long insuranceId) {
		return insuranceService.delete(insuranceId);
	}

}
