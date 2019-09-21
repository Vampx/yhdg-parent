package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeBatteryForegiftService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 换电押金
 */
@Controller
@RequestMapping(value = "/security/hdg/exchange_battery_foregift")
public class ExchangeBatteryForegiftController extends SecurityController {
	@Autowired
	ExchangeBatteryForegiftService exchangeBatteryForegiftService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;
	@Autowired
	AgentService agentService;
	@Autowired
	CabinetService cabinetService;

	@RequestMapping(value = "find_foregift_list.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findForegiftList(Integer batteryType, Integer agentId) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		if (exchangeBatteryForegiftList != null && exchangeBatteryForegiftList.size() > 0) {
			return DataResult.successResult(exchangeBatteryForegiftList.get(0));
		} else {
			return ExtResult.failResult("无押金信息");
		}
	}

	@RequestMapping(value = "exchange_battery_foregift.htm")
	public void exchangeBatteryForegfit(Model model, Integer batteryType, Integer agentId) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "exchange_battery_foregift_add.htm")
	public void exchangeBatteryForegfitAdd(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("typeName", agentBatteryType.getTypeName());
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("exchange_battery_foregift_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryForegfitCreate(ExchangeBatteryForegift entity) {
		return exchangeBatteryForegiftService.create(entity);
	}

	@RequestMapping(value = "exchange_battery_foregift_edit.htm")
	public String exchangeBatteryForegfitEdit(Model model, Long batteryForegiftId) {
		ExchangeBatteryForegift entity = exchangeBatteryForegiftService.find(batteryForegiftId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(entity.getBatteryType(), entity.getAgentId());
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("typeName", agentBatteryType.getTypeName());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/exchange_battery_foregift/exchange_battery_foregift_edit";
	}

	@RequestMapping("exchange_battery_foregift_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryForegfitUpdate(ExchangeBatteryForegift entity) {
		return exchangeBatteryForegiftService.update(entity);
	}

	@RequestMapping("exchange_battery_foregift_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangeBatteryForegfitDelete(Long batteryForegiftId) {
		return exchangeBatteryForegiftService.delete(batteryForegiftId);
	}
}
