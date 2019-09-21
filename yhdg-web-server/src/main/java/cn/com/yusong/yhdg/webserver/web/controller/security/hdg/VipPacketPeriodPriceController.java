package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPacketPeriodPriceRenewService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPacketPeriodPriceService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
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
	@Autowired
	VipPacketPeriodPriceRenewService vipPacketPeriodPriceRenewService;


	@RequestMapping(value = "vip_exchange_packet_period_price.htm")
	public void vipExchangePacketPeriodPrice(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId, Long vipForegiftId) {
		List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findListByVipForegiftId(vipForegiftId);
		for (VipPacketPeriodPrice vipPacketPeriodPrice : packetPeriodPriceList) {
			vipPacketPeriodPrice.setRenewList(vipPacketPeriodPriceRenewService.findList(vipPacketPeriodPrice.getId()));
		}
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@RequestMapping(value = "vip_exchange_packet_period_price_edit.htm")
	public void vipExchangePacketPeriodPricEdit1(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId, Long vipForegiftId) {
		List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId, priceId, vipForegiftId);
		for (VipPacketPeriodPrice vipPacketPeriodPrice : packetPeriodPriceList) {
			vipPacketPeriodPrice.setRenewList(vipPacketPeriodPriceRenewService.findList(vipPacketPeriodPrice.getId()));
		}
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
		model.addAttribute("vipForegiftId", vipForegiftId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "vip_exchange_packet_period_price_add_first.htm")
	public void exchangePacketPeriodPriceAddFirst(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long vipForegiftId, Long priceId) {
		model.addAttribute("vipForegiftId", vipForegiftId);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "vip_exchange_packet_period_price_add_second.htm")
	public void exchangePacketPeriodPriceSecondAdd(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long packetPriceId) {
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("packetPriceId", packetPriceId);
	}


	@RequestMapping("vip_exchange_packet_period_price_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceCreate(VipPacketPeriodPrice entity) {
		return vipPacketPeriodPriceService.create(entity);
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


	@RequestMapping("exchange_packet_period_price_second_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceSecondCreate(VipPacketPeriodPriceRenew entity) {
		return vipPacketPeriodPriceRenewService.create(entity);
	}

	@RequestMapping(value = "vip_exchange_packet_period_second_price.htm")
	public void exchangePacketPeriodSecondPrice(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long packetPriceId) {
		List<VipPacketPeriodPriceRenew> list = vipPacketPeriodPriceRenewService.findList(packetPriceId);
		model.addAttribute("packetPeriodPriceSecondList", list);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("packetPriceId", packetPriceId);
	}

	@RequestMapping(value = "vip_exchange_packet_period_second_price_edit.htm")
	public String exchangePacketPeriodSecondPriceEdit(Model model, Long exchangeId) {
		VipPacketPeriodPriceRenew entity = vipPacketPeriodPriceRenewService.find(exchangeId);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/vip_packet_period_price/vip_exchange_packet_period_second_price_edit";
	}

	@RequestMapping("exchange_packet_period_second_price_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodSecondPriceUpdate(VipPacketPeriodPriceRenew entity) {
		return vipPacketPeriodPriceRenewService.update(entity);
	}

	@RequestMapping("exchange_packet_period_second_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodSecondPriceDelete(Long exchangeId) {
		return vipPacketPeriodPriceRenewService.delete(exchangeId);
	}

	@RequestMapping(value = "vip_exchange_packet_period_first_price_edit.htm")
	public String exchangePacketPeriodFirstPriceEdit(Model model, Long exchangeId) {
		VipPacketPeriodPrice entity = vipPacketPeriodPriceService.find(exchangeId);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		model.addAttribute("vipForegiftId", entity.getVipForegiftId());
		model.addAttribute("foregiftId", entity.getForegiftId());
		model.addAttribute("batteryType", entity.getBatteryType());
		model.addAttribute("agentId", entity.getAgentId());
		return "/security/hdg/vip_packet_period_price/vip_exchange_packet_period_first_price_edit";
	}


	@RequestMapping("vip_exchange_packet_period_first_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodFirstPriceDelete(Long exchangeId) {
		return vipPacketPeriodPriceService.delete(exchangeId);
	}
}
