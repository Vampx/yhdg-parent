package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordService;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import cn.com.yusong.yhdg.webserver.service.zd.*;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping(value = "/security/zd/rent_installment_setting")
public class RentInstallmentSettingController extends SecurityController {
	@Autowired
	RentInstallmentSettingService rentInstallmentSettingService;
	@Autowired
	RentBatteryForegiftService rentBatteryForegiftService;
	@Autowired
	RentPeriodPriceService rentPeriodPriceService;
	@Autowired
	RentInsuranceService rentInsuranceService;
	@Autowired
	RentInstallmentDetailService rentInstallmentDetailService;
	@Autowired
	CustomerInstallmentRecordService customerInstallmentRecordService;
	@Autowired
	CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

	@SecurityControl(limits = "zd.RentInstallmentSetting:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "zd.RentInstallmentSetting:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(RentInstallmentSetting search) {
		return PageResult.successResult(rentInstallmentSettingService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON)
	@RequestMapping(value = "unique.htm")
	public ExtResult unique(Long id, String mobile) {
		boolean unique = rentInstallmentSettingService.findUnique(id, mobile);
		if (unique) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("该用户已设置过分期");
		}
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		RentInstallmentSetting entity = rentInstallmentSettingService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/zd/rent_installment_setting/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("defaultNum", RentInstallmentSetting.Num.THREE.getValue());
		model.addAttribute("NumEnum", RentInstallmentSetting.Num.values());
	}

	@RequestMapping("find_price_info.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findPriceInfo(Integer agentId, Integer batteryType) {
		RentBatteryForegift rentBatteryForegift = null;
		RentPeriodPrice rentPeriodPrice = null;
		RentInsurance rentInsurance = null;

		List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		if (rentBatteryForegiftList.size() > 0) {
			rentBatteryForegift = rentBatteryForegiftList.get(0);
			List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findListByForegiftId(rentBatteryForegiftList.get(0).getId(), batteryType, agentId);
			if (rentPeriodPriceList.size() > 0) {
				rentPeriodPrice = rentPeriodPriceList.get(0);
			}
		}
		List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
		if (rentInsuranceList.size() > 0) {
			rentInsurance = rentInsuranceList.get(0);
		}
		Object[] objs = {(rentBatteryForegift), (rentPeriodPrice), (rentInsurance)};
		return DataResult.successResult(objs);
	}

	@RequestMapping(value = "rent_battery_foregift.htm")
	public void rentBatteryForegfit(Model model, Integer batteryType, Integer agentId) {
		List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("rentBatteryForegiftList", rentBatteryForegiftList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "rent_packet_period_price.htm")
	public void rentPacketPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		model.addAttribute("rentPeriodPriceList", rentPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "rent_insurance.htm")
	public void rentInsurance(Model model, Integer batteryType, Integer agentId) {
		List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("rentInsuranceList", rentInsuranceList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_rent_battery_foregift.htm")
	public void editRentBatteryForegfit(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("rentBatteryForegiftList", rentBatteryForegiftList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_rent_packet_period_price.htm")
	public void editRentPacketPeriodPrice(Model model, Long foregiftId, Long packetId, Integer batteryType, Integer agentId) {
		List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		model.addAttribute("rentPeriodPriceList", rentPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("packetId", packetId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_rent_insurance.htm")
	public void editRentBatteryTypeInsurance(Model model, Long insuranceId, Integer batteryType, Integer agentId) {
		List<RentInsurance> rentInsuranceList = rentInsuranceService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("rentInsuranceList", rentInsuranceList);
		model.addAttribute("insuranceId", insuranceId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(String data) throws IOException, ParseException {
		return rentInstallmentSettingService.create(data);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		RentInstallmentSetting entity = rentInstallmentSettingService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			List<RentInstallmentDetail> rentInstallmentDetailList = rentInstallmentDetailService.findListBySettingId(id);
			for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
				CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.findByRentSettingId(rentInstallmentDetail.getSettingId(), ConstEnum.Category.RENT.getValue());
				if (customerInstallmentRecord != null) {
					CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = customerInstallmentRecordPayDetailService.findByRecordIdAndNum(customerInstallmentRecord.getId(), rentInstallmentDetail.getNum(), ConstEnum.Category.RENT.getValue());
					if (customerInstallmentRecordPayDetail != null) {
						rentInstallmentDetail.setPayStatusName(customerInstallmentRecordPayDetail.getStatusName());
					}
				}
			}
			model.addAttribute("rentInstallmentDetailList", rentInstallmentDetailList);
			model.addAttribute("defaultNum", rentInstallmentDetailList.size());
			model.addAttribute("NumEnum", RentInstallmentSetting.Num.values());
			model.addAttribute("entity", entity);
		}
		return "/security/zd/rent_installment_setting/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(String data) throws IOException, ParseException {
		return rentInstallmentSettingService.update(data);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id) {
		return rentInstallmentSettingService.delete(id);
	}

	@RequestMapping(value = "view_pay_detail.htm")
	public String viewPayDetail(Model model, Long settingId, Integer num) {
		CustomerInstallmentRecordPayDetail entity = null;
		CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.findByRentSettingId(settingId, ConstEnum.Category.RENT.getValue());
		if (customerInstallmentRecord != null) {
			entity = customerInstallmentRecordPayDetailService.findByRecordIdAndNum(customerInstallmentRecord.getId(), num, ConstEnum.Category.RENT.getValue());
		}
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/zd/rent_installment_setting/view_pay_detail";
	}

	@RequestMapping("find_packet_info.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult page(Long foregiftId, Integer batteryType, Integer agentId) {
		List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		if (rentPeriodPriceList.size() > 0) {
			return DataResult.successResult(rentPeriodPriceList.get(0));
		} else {
			return ExtResult.failResult("押金信息不存在");
		}
	}
}
