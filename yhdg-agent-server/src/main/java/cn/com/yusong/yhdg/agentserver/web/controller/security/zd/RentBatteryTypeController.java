package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryForegiftService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentInsuranceService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodPriceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
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

@Controller
@RequestMapping(value = "/security/zd/rent_battery_type")
public class RentBatteryTypeController extends SecurityController {
	@Autowired
	private RentBatteryTypeService rentBatteryTypeService;
	@Autowired
    private RentBatteryForegiftService rentBatteryForegiftService;
	@Autowired
	private RentPeriodPriceService rentPeriodPriceService;
	@Autowired
    private RentInsuranceService rentInsuranceService;

	@SecurityControl(limits = "zd.RentBatteryType:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "zd.RentBatteryType:list");
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_battery_type.htm")
	public void selectBatteryType(Model model, Integer agentId) {
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(RentBatteryType search) {
		return PageResult.successResult(rentBatteryTypeService.findPage(search));
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Integer batteryType, Integer agentId) {
		RentBatteryType entity = rentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/zd/rent_battery_type/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(RentBatteryType entity) {
		return rentBatteryTypeService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Integer batteryType, Integer agentId, Integer editFlag) {
		model.addAttribute("editFlag", editFlag);
		List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		if (rentBatteryForegiftList == null || rentBatteryForegiftList.size() == 0) {
			model.addAttribute("foregiftColourFlag", ConstEnum.Flag.FALSE.getValue());
		} else {
			model.addAttribute("foregiftColourFlag", ConstEnum.Flag.TRUE.getValue());
		}

		List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
		if (rentInsuranceList == null || rentInsuranceList.size() == 0) {
			model.addAttribute("insuranceColourFlag", ConstEnum.Flag.FALSE.getValue());
		} else {
			model.addAttribute("insuranceColourFlag", ConstEnum.Flag.TRUE.getValue());
		}

		RentBatteryType entity = rentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}

		return "/security/zd/rent_battery_type/edit";
	}

	@RequestMapping(value = "edit_basic.htm")
	public String editBasic(Model model, Integer batteryType, Integer agentId) {
		RentBatteryType entity = rentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/zd/rent_battery_type/edit_basic";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(RentBatteryType entity) {
		return rentBatteryTypeService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Integer batteryType, Integer agentId) {
		return rentBatteryTypeService.delete(batteryType, agentId);
	}

	@RequestMapping(value = "edit_rent_period_price.htm")
	public String editPacketPeriodPrice(Model model, Integer batteryType, Integer agentId) {
		List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		List<RentPeriodPrice> rentPeriodPriceList = null;
		if (!rentBatteryForegiftList.isEmpty() && rentBatteryForegiftList.size() > 0) {
			rentPeriodPriceList = rentPeriodPriceService.findListByForegift(rentBatteryForegiftList.get(0).getId().longValue(), batteryType, agentId);
		}

		RentBatteryType entity = rentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("rentBatteryForegiftList", rentBatteryForegiftList);
			model.addAttribute("rentPeriodPriceList", rentPeriodPriceList);
			model.addAttribute("entity", entity);
		}

		return "/security/zd/rent_battery_type/edit_rent_period_price";
	}

	@RequestMapping(value = "edit_rent_insurance.htm")
	public String editRentInsurance(Model model, Integer batteryType, Integer agentId) {
		List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
		RentBatteryType entity = rentBatteryTypeService.find(batteryType, agentId);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
			model.addAttribute("rentInsuranceList", rentInsuranceList);
			model.addAttribute("batteryType", batteryType);
			model.addAttribute("agentId", agentId);
		}

		return "/security/zd/rent_battery_type/edit_rent_insurance";
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "tree.htm")
	public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		Set<Integer> checkedSet = Collections.emptySet();
		rentBatteryTypeService.tree(checkedSet, dummy, agentId, response.getOutputStream());
	}

}
