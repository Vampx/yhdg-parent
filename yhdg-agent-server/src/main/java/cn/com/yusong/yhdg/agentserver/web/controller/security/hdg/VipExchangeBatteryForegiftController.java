package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.ExchangeBatteryForegiftService;
import cn.com.yusong.yhdg.agentserver.service.hdg.VipExchangeBatteryForegiftService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip换电押金
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_exchange_battery_foregift")
public class VipExchangeBatteryForegiftController extends SecurityController {
	@Autowired
	ExchangeBatteryForegiftService exchangeBatteryForegiftService;
	@Autowired
	VipExchangeBatteryForegiftService vipExchangeBatteryForegiftService;
	@Autowired
	AgentService agentService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;

	@RequestMapping(value = "vip_exchange_battery_foregift.htm")
	public void vipExchangeBatteryForegfit(Model model, Integer batteryType, Integer agentId, Long priceId, Integer index) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByVipBatteryType(batteryType, agentId, priceId);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
		model.addAttribute("index", index);
	}

	@RequestMapping(value = "vip_exchange_battery_foregift_edit.htm")
	public String exchangePacketPeriodPriceEdit(Model model, Integer exchangeId,Long foregiftId, Long priceId) {
		ExchangeBatteryForegift entity = exchangeBatteryForegiftService.find(foregiftId);
		VipExchangeBatteryForegift vip = vipExchangeBatteryForegiftService.find(Long.valueOf(exchangeId));
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(entity.getBatteryType(), entity.getAgentId());

		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
			model.addAttribute("priceId", priceId);
			model.addAttribute("foregiftId", foregiftId);
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("agentId", agent.getId());
			model.addAttribute("typeName", agentBatteryType.getTypeName());
			if (vip != null) {
				model.addAttribute("reduceMoney", vip.getReduceMoney());
				model.addAttribute("id", vip.getId());
				model.addAttribute("memo", vip.getMemo());
			} else {
				model.addAttribute("reduceMoney", 0);
				model.addAttribute("memo", "");
				model.addAttribute("id", 0);

			}
		}
		return "/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_edit";
	}

	@RequestMapping("vip_exchange_battery_foregift_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult vipExchangeBatteryForegift(VipExchangeBatteryForegift entity) {
		return vipExchangeBatteryForegiftService.create(entity);
	}
}
