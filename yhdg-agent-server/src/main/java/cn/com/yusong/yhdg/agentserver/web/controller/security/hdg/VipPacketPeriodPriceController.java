package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.hdg.VipPacketPeriodPriceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip包时段套餐
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_packet_period_price")
public class VipPacketPeriodPriceController extends SecurityController {

    @Autowired
	VipPacketPeriodPriceService vipPacketPeriodPriceService;
	@Autowired
	AgentService agentService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_exchange_packet_period_price.htm")
	public void vipExchangePacketPeriodPrice(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
		List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId, priceId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "vip_exchange_packet_period_price_add.htm")
	public void exchangePacketPeriodPriceAdd(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("typeName", agentBatteryType.getTypeName());
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@RequestMapping("vip_exchange_packet_period_price_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceCreate(VipPacketPeriodPrice entity) {
		return vipPacketPeriodPriceService.create(entity);
	}

	@RequestMapping(value = "vip_exchange_packet_period_price_edit.htm")
	public String exchangePacketPeriodPriceEdit(Model model, Long exchangeId) {
		VipPacketPeriodPrice entity = vipPacketPeriodPriceService.find(exchangeId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(entity.getBatteryType(), entity.getAgentId());
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("typeName", agentBatteryType.getTypeName());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_edit";
	}

	@RequestMapping("vip_exchange_packet_period_price_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceUpdate(VipPacketPeriodPrice entity) {
		return vipPacketPeriodPriceService.update(entity);
	}

	@RequestMapping("vip_exchange_packet_period_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceDelete(Long exchangeId) {
		return vipPacketPeriodPriceService.delete(exchangeId);
	}

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return vipPacketPeriodPriceService.delete(id);
    }

}
