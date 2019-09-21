package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.hdg.*;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String edit(Model model, Integer batteryType, Integer agentId, Integer editFlag) {
		model.addAttribute("editFlag", editFlag);
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		if (exchangeBatteryForegiftList == null || exchangeBatteryForegiftList.size() == 0) {
			model.addAttribute("foregiftColourFlag", ConstEnum.Flag.FALSE.getValue());
		} else {
			model.addAttribute("foregiftColourFlag", ConstEnum.Flag.TRUE.getValue());
		}
		ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(batteryType, agentId);
		if (exchangePriceTime == null) {
			model.addAttribute("priceTimeColourFlag", ConstEnum.Flag.FALSE.getValue());
		} else {
			model.addAttribute("priceTimeColourFlag", ConstEnum.Flag.TRUE.getValue());
		}
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		if (insuranceList == null || insuranceList.size() == 0) {
			model.addAttribute("insuranceColourFlag", ConstEnum.Flag.FALSE.getValue());
		} else {
			model.addAttribute("insuranceColourFlag", ConstEnum.Flag.TRUE.getValue());
		}
		List<Cabinet> cabinetList = cabinetService.findListByBatteryType(batteryType);
		if (cabinetList.size() == 0) {
			model.addAttribute("cabinetListColourFlag", ConstEnum.Flag.FALSE.getValue());
		}else {
			model.addAttribute("cabinetListColourFlag", ConstEnum.Flag.TRUE.getValue());
		}
		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/edit";
	}

	@RequestMapping(value = "edit_packet_period_price.htm")
	public String editPacketPeriodPrice(Model model, Integer batteryType, Integer agentId) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
//		if (exchangeBatteryForegift == null) {
//			model.addAttribute("volumeColourFlag", ConstEnum.Flag.FALSE.getValue());
//		} else {
//			model.addAttribute("volumeColourFlag", ConstEnum.Flag.TRUE.getValue());
//		}

		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/edit_packet_period_price";
	}

	@RequestMapping(value = "edit_price_time.htm")
	public String editPriceTime(Model model, Integer batteryType, Integer agentId) {
		ExchangePriceTime entity = exchangePriceTimeService.findByBatteryType(batteryType, agentId);
		AgentBatteryType agentBatteryType = agentBatteryTypeService.findForName(batteryType, agentId);
		model.addAttribute("entity", entity);
		model.addAttribute("agentBatteryType", agentBatteryType);
		return "/security/basic/agent_battery_type/edit_price_time";
	}

	@RequestMapping(value = "edit_insurance.htm")
	public String editInsurance(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/edit_insurance";
	}

	@RequestMapping(value = "edit_cabinet.htm")
	public String editCabinet(Model model, Integer batteryType, Integer agentId) {
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
		return "/security/basic/agent_battery_type/edit_cabinet";
	}

	@RequestMapping(value = "edit_basic.htm")
	public String editBasic(Model model, Integer batteryType, Integer agentId) {
		AgentBatteryType entity = agentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/agent_battery_type/edit_basic";
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

}
