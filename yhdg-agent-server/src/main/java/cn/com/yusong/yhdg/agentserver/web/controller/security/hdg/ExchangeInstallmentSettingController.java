package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerInstallmentRecordService;
import cn.com.yusong.yhdg.agentserver.service.hdg.*;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_setting")
public class ExchangeInstallmentSettingController extends SecurityController {
	@Autowired
	ExchangeInstallmentSettingService exchangeInstallmentSettingService;
	@Autowired
	ExchangeBatteryForegiftService exchangeBatteryForegiftService;
	@Autowired
	PacketPeriodPriceService packetPeriodPriceService;
	@Autowired
	InsuranceService insuranceService;
	@Autowired
	ExchangeInstallmentDetailService exchangeInstallmentDetailService;
	@Autowired
	CustomerInstallmentRecordService customerInstallmentRecordService;
	@Autowired
	CustomerInstallmentRecordPayDetailService customerInstallmentRecordPayDetailService;

	@SecurityControl(limits = "hdg.ExchangeInstallmentSetting:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentSetting:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(ExchangeInstallmentSetting search) {
		return PageResult.successResult(exchangeInstallmentSettingService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON)
	@RequestMapping(value = "unique.htm")
	public ExtResult unique(Long id, String mobile) {
		boolean unique = exchangeInstallmentSettingService.findUnique(id, mobile);
		if (unique) {
			return ExtResult.successResult();
		} else {
			return ExtResult.failResult("该用户已设置过分期");
		}
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		ExchangeInstallmentSetting entity = exchangeInstallmentSettingService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/exchange_installment_setting/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("defaultNum", ExchangeInstallmentSetting.Num.THREE.getValue());
		model.addAttribute("NumEnum", ExchangeInstallmentSetting.Num.values());
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_price.htm")
	public void selectPrice(Model model, Integer agentId, Integer batteryType) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		if (exchangeBatteryForegiftList.size() > 0) {
			model.addAttribute("exchangeBatteryForegift", exchangeBatteryForegiftList.get(0));
			List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(exchangeBatteryForegiftList.get(0).getId().intValue(), batteryType, agentId);
			if (packetPeriodPriceList.size() > 0) {
				model.addAttribute("packetPeriodPrice", packetPeriodPriceList.get(0));
			}
		}
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		if (insuranceList.size() > 0) {
			model.addAttribute("insurance", insuranceList.get(0));
		}
		model.addAttribute("agentId", agentId);
		model.addAttribute("batteryType", batteryType);
	}

	@RequestMapping("find_price_info.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findPriceInfo(Integer agentId, Integer batteryType) {
		ExchangeBatteryForegift exchangeBatteryForegift = null;
		PacketPeriodPrice packetPeriodPrice = null;
		Insurance insurance = null;

		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		if (exchangeBatteryForegiftList.size() > 0) {
			exchangeBatteryForegift = exchangeBatteryForegiftList.get(0);
			List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(exchangeBatteryForegiftList.get(0).getId().intValue(), batteryType, agentId);
			if (packetPeriodPriceList.size() > 0) {
				packetPeriodPrice = packetPeriodPriceList.get(0);
			}
		}
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		if (insuranceList.size() > 0) {
			insurance = insuranceList.get(0);
		}
		Object[] objs = {(exchangeBatteryForegift), (packetPeriodPrice), (insurance)};
		return DataResult.successResult(objs);
	}

	@RequestMapping(value = "exchange_battery_foregift.htm")
	public void exchangeBatteryForegfit(Model model, Integer batteryType, Integer agentId) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "exchange_packet_period_price.htm")
	public void exchangePacketPeriodPrice(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId.intValue(), batteryType, agentId);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "exchange_insurance.htm")
	public void exchangeBatteryTypeInsurance(Model model, Integer batteryType, Integer agentId) {
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("insuranceList", insuranceList);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_exchange_battery_foregift.htm")
	public void editExchangeBatteryForegfit(Model model, Long foregiftId, Integer batteryType, Integer agentId) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_exchange_packet_period_price.htm")
	public void editExchangePacketPeriodPrice(Model model, Long foregiftId, Long packetId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId.intValue(), batteryType, agentId);
		model.addAttribute("packetPeriodPriceList", packetPeriodPriceList);
		model.addAttribute("foregiftId", foregiftId);
		model.addAttribute("packetId", packetId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "edit_exchange_insurance.htm")
	public void editExchangeBatteryTypeInsurance(Model model, Long insuranceId, Integer batteryType, Integer agentId) {
		List<Insurance> insuranceList = insuranceService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("insuranceList", insuranceList);
		model.addAttribute("insuranceId", insuranceId);
		model.addAttribute("batteryType", batteryType);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(String data) throws IOException, ParseException {
		return exchangeInstallmentSettingService.create(data);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		ExchangeInstallmentSetting entity = exchangeInstallmentSettingService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = exchangeInstallmentDetailService.findListBySettingId(id);
			for (ExchangeInstallmentDetail exchangeInstallmentDetail : exchangeInstallmentDetailList) {
				CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.findByExchangeSettingId(exchangeInstallmentDetail.getSettingId(), Battery.Category.EXCHANGE.getValue());
				if (customerInstallmentRecord != null) {
					CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail = customerInstallmentRecordPayDetailService.findByRecordIdAndNum(customerInstallmentRecord.getId(), exchangeInstallmentDetail.getNum(), ConstEnum.Category.EXCHANGE.getValue());
					if (customerInstallmentRecordPayDetail != null) {
						exchangeInstallmentDetail.setPayStatusName(customerInstallmentRecordPayDetail.getStatusName());
					}
				}
			}
			model.addAttribute("exchangeInstallmentDetailList", exchangeInstallmentDetailList);
			model.addAttribute("defaultNum", exchangeInstallmentDetailList.size());
			model.addAttribute("NumEnum", ExchangeInstallmentSetting.Num.values());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/exchange_installment_setting/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(String data) throws IOException, ParseException {
		return exchangeInstallmentSettingService.update(data);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id) {
		return exchangeInstallmentSettingService.delete(id);
	}

	@RequestMapping(value = "view_pay_detail.htm")
	public String viewPayDetail(Model model, Long settingId, Integer num) {
		CustomerInstallmentRecordPayDetail entity = null;
		CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordService.findByExchangeSettingId(settingId, ConstEnum.Category.EXCHANGE.getValue());
		if (customerInstallmentRecord != null) {
			entity = customerInstallmentRecordPayDetailService.findByRecordIdAndNum(customerInstallmentRecord.getId(), num, ConstEnum.Category.EXCHANGE.getValue());
		}
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/exchange_installment_setting/view_pay_detail";
	}

	@RequestMapping("find_packet_info.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult page(Long foregiftId, Integer batteryType, Integer agentId) {
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId.intValue(), batteryType, agentId);
		if (packetPeriodPriceList.size() > 0) {
			return DataResult.successResult(packetPeriodPriceList.get(0));
		} else {
			return ExtResult.failResult("押金信息不存在");
		}
	}
}
