package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *  电池类型
 */
@Controller
@RequestMapping(value = "/security/basic/agent_battery_type")
public class AgentBatteryTypeController extends SecurityController {
	@Autowired
	AgentBatteryTypeService agentBatteryTypeService;
	@Autowired
	ExchangeBatteryForegiftService exchangeBatteryForegiftService;
	@Autowired
	ExchangeWhiteListService exchangeWhiteListService;
	@Autowired
	ExchangePriceTimeService exchangePriceTimeService;
	@Autowired
	InsuranceService insuranceService;
	@Autowired
	CabinetService cabinetService;
	@Autowired
	VipPriceShopService vipPriceShopService;
	@Autowired
	AgentService agentService;
	@Autowired
	PacketPeriodPriceService packetPeriodPriceService;
	@Autowired
	PacketPeriodPriceRenewService packetPeriodPriceRenewService;
	@Autowired
	CabinetBatteryTypeService cabinetBatteryTypeService;
	@Autowired
	StationBatteryTypeService stationBatteryTypeService;

	@SecurityControl(limits = "basic.AgentBatteryType:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "basic.AgentBatteryType:list");
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_battery_type.htm")
	public void selectBatteryType(Model model, Integer agentId) {
		model.addAttribute("agentId", agentId);
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "tree.htm")
	public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		Set<Integer> checkedSet = Collections.emptySet();
		agentBatteryTypeService.tree(checkedSet, dummy, agentId, response.getOutputStream());
	}


	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(AgentBatteryType search) {
		return PageResult.successResult(agentBatteryTypeService.findPage(search));
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "unbind_battery_type_page.htm")
	public void unbindCabinetPage(Model model, Integer agentId, String cabinetId) {
		model.addAttribute("agentId", agentId);
		model.addAttribute("cabinetId", cabinetId);
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(batteryType, agentId);
		if (exchangePriceTime != null) {
			entity.setActiveSingleExchange(exchangePriceTime.getActiveSingleExchange());
			entity.setTimesPrice(exchangePriceTime.getTimesPrice());
		}
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
	}

	@RequestMapping("batch_bind_cabinet.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult batchBindCabinet(String cabinetId, Integer[] batteryTypeList) {
		int count = 0;
		for (Integer e : batteryTypeList) {
			count += agentBatteryTypeService.bindCabinet(e, cabinetId);
		}
		cabinetService.updatePrice(cabinetService.find(cabinetId).getAgentId(),cabinetId);
		Cabinet cabinet = cabinetService.find(cabinetId);
		if (cabinet != null && cabinet.getShopId() != null) {
			VipPriceShop vipPriceShop = vipPriceShopService.findByShopId(cabinet.getShopId());
			if (vipPriceShop != null) {
				cabinetService.updateShopPrice(vipPriceShop.getPriceId(), vipPriceShop.getShopId());
			}else {
				cabinetService.updateShopPriceByCabint(cabinet.getShopId());
			}
		}
		return ExtResult.successResult(String.format("成功绑定%d个换电站", count));
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(AgentBatteryType entity) {
		return agentBatteryTypeService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(batteryType, agentId);
		if (exchangePriceTime != null) {
			entity.setActiveSingleExchange(exchangePriceTime.getActiveSingleExchange());
			entity.setTimesPrice(exchangePriceTime.getTimesPrice());
		}
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(AgentBatteryType entity) {
		return agentBatteryTypeService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Integer batteryType, Integer agentId) {
		return agentBatteryTypeService.delete(batteryType, agentId);
	}

	@RequestMapping(value = "find_agent_battery_type.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult agentBatteryType(Integer batteryType, Integer agentId) {
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		if (agentBatteryType == null) {
			return DataResult.successResult();
		} else {
			return ExtResult.failResult("该运营商已存在该电池类型，请重新选择");
		}
	}

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

	@RequestMapping(value = "view_exchange_battery_foregift.htm")
	public void viewExchangeBatteryForegfit(Model model, Integer batteryType, Integer agentId) {
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
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			Agent agent = agentService.find(entity.getAgentId());
			model.addAttribute("agentName", agent.getAgentName());
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/exchange_battery_foregift_edit";
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

	@RequestMapping(value = "exchange_battery_foregift_memo.htm")
	public void exchangeBatteryForegiftMemo(Model model, Long batteryForegiftId) {
		ExchangeBatteryForegift entity = exchangeBatteryForegiftService.find(batteryForegiftId);
		model.addAttribute("memo", entity.getMemo());
	}

	@RequestMapping(value = "exchange_packet_period_price.htm")
	public void exchangePacketPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		for (PacketPeriodPrice packetPeriodPrice : packetPeriodPriceList) {
			packetPeriodPrice.setRenewList(packetPeriodPriceRenewService.findList(packetPeriodPrice.getId()));
		}
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "view_exchange_packet_period_price.htm")
	public void viewExchangePacketPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		for (PacketPeriodPrice packetPeriodPrice : packetPeriodPriceList) {
			packetPeriodPrice.setRenewList(packetPeriodPriceRenewService.findList(packetPeriodPrice.getId()));
		}
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", agentBatteryType);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "exchange_packet_period_price_add_first.htm")
	public void exchangePacketPeriodPriceAdd(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
		model.addAttribute("priceId", priceId);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "exchange_packet_period_price_add_second.htm")
	public void exchangePacketPeriodPriceSecondAdd(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@RequestMapping("exchange_packet_period_price_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceCreate(PacketPeriodPrice entity) {
		return packetPeriodPriceService.create(entity);
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

	@RequestMapping("exchange_packet_period_price_second_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodPriceSecondCreate(PacketPeriodPriceRenew entity) {
		return packetPeriodPriceRenewService.create(entity);
	}

	@RequestMapping(value = "exchange_packet_period_second_price.htm")
	public void exchangePacketPeriodSecondPrice(Model model, Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
		List<PacketPeriodPriceRenew> list = packetPeriodPriceRenewService.findList(priceId);
		model.addAttribute("packetPeriodPriceSecondList", list);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("priceId", priceId);
	}

	@RequestMapping(value = "exchange_packet_period_second_price_edit.htm")
	public String exchangePacketPeriodSecondPriceEdit(Model model, Long exchangeId) {
		PacketPeriodPriceRenew entity = packetPeriodPriceRenewService.find(exchangeId);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/exchange_packet_period_second_price_edit";
	}

	@RequestMapping("exchange_packet_period_second_price_update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodSecondPriceUpdate(PacketPeriodPriceRenew entity) {
		return packetPeriodPriceRenewService.update(entity);
	}

	@RequestMapping("exchange_packet_period_second_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodSecondPriceDelete(Long exchangeId) {
		return packetPeriodPriceRenewService.delete(exchangeId);
	}

	@RequestMapping(value = "exchange_packet_period_first_price_edit.htm")
	public String exchangePacketPeriodFirstPriceEdit(Model model, Long exchangeId) {
		PacketPeriodPrice entity = packetPeriodPriceService.find(exchangeId);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		model.addAttribute("foregiftId", entity.getForegiftId());
		model.addAttribute("batteryType", entity.getBatteryType());
		model.addAttribute("agentId", entity.getAgentId());
		return "/security/basic/agent_battery_type/exchange_packet_period_first_price_edit";
	}


	@RequestMapping("exchange_packet_period_first_price_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult exchangePacketPeriodFirstPriceDelete(Long exchangeId) {
		return packetPeriodPriceService.delete(exchangeId);
	}

	@RequestMapping(value = "price_cabinet.htm")
	public void priceCabinet(Model model, Integer batteryType, Integer agentId) {
		List<CabinetBatteryType> priceCabinetList = cabinetBatteryTypeService.findListByBatteryType(batteryType, agentId);
		for (CabinetBatteryType cabinetBatteryType : priceCabinetList) {
			cabinetBatteryType.setAgentId(agentId);
		}
		model.addAttribute("priceCabinetList", priceCabinetList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "view_price_cabinet.htm")
	public void viewPriceCabinet(Model model, Integer batteryType, Integer agentId) {
		List<CabinetBatteryType> priceCabinetList = cabinetBatteryTypeService.findListByBatteryType(batteryType, agentId);
		for (CabinetBatteryType cabinetBatteryType : priceCabinetList) {
			cabinetBatteryType.setAgentId(agentId);
		}
		model.addAttribute("priceCabinetList", priceCabinetList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("price_cabinet_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult priceCabinetCreate(CabinetBatteryType entity) {
		return cabinetBatteryTypeService.create(entity);
	}

	@RequestMapping("price_cabinet_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult priceCabinetDelete(String cabinetId, Integer batteryType) {
		return cabinetBatteryTypeService.delete(cabinetId, batteryType);
	}


	@RequestMapping(value = "price_station.htm")
	public void priceStation(Model model, Integer batteryType, Integer agentId) {
		List<StationBatteryType> priceStationList = stationBatteryTypeService.findListByBatteryType(batteryType, agentId);
		for (StationBatteryType stationBatteryType : priceStationList) {
			stationBatteryType.setAgentId(agentId);
		}
		model.addAttribute("priceStationList", priceStationList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "view_price_station.htm")
	public void viewPriceStation(Model model, Integer batteryType, Integer agentId) {
		List<StationBatteryType> priceStationList = stationBatteryTypeService.findListByBatteryType(batteryType, agentId);
		for (StationBatteryType stationBatteryType : priceStationList) {
			stationBatteryType.setAgentId(agentId);
		}
		model.addAttribute("priceStationList", priceStationList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("price_station_create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult priceStationCreate(StationBatteryType entity) {
		return stationBatteryTypeService.create(entity);
	}

	@RequestMapping("price_station_delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult priceStationDelete(String stationId, Integer batteryType) {
		return stationBatteryTypeService.delete(stationId, batteryType);
	}
}
