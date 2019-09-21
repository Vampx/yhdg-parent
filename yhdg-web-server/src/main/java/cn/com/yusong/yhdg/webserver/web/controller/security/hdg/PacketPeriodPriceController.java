package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodPriceService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 包时段套餐
 */
@Controller
@RequestMapping(value = "/security/hdg/packet_period_price")
public class PacketPeriodPriceController extends SecurityController {

    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
	@Autowired
	AgentService agentService;
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;


    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "exchange_packet_period_price.htm")
	public void exchangePacketPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "exchange_packet_period_price_add.htm")
	public void exchangePacketPeriodPriceAdd(Model model, Integer foregiftId, Integer batteryType, Integer agentId) {
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("typeName", agentBatteryType.getTypeName());
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("exchange_packet_period_price_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceCreate(PacketPeriodPrice entity) {
		return packetPeriodPriceService.create(entity);
	}

	@RequestMapping(value = "exchange_packet_period_price_edit.htm")
	public String exchangePacketPeriodPriceEdit(Model model, Long exchangeId) {
		PacketPeriodPrice entity = packetPeriodPriceService.find(exchangeId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(entity.getBatteryType(), entity.getAgentId());
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("typeName", agentBatteryType.getTypeName());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/packet_period_price/exchange_packet_period_price_edit";
	}

	@RequestMapping("exchange_packet_period_price_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceUpdate(PacketPeriodPrice entity) {
		return packetPeriodPriceService.update(entity);
	}

	@RequestMapping("exchange_packet_period_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceDelete(Long exchangeId) {
		return packetPeriodPriceService.delete(exchangeId);
	}

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return packetPeriodPriceService.delete(id);
    }

}
