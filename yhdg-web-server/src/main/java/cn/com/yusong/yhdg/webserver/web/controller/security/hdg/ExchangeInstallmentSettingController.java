package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecord;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordPayDetailMapper;
import cn.com.yusong.yhdg.webserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordPayDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerInstallmentRecordService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	//新加四个
	@Autowired
	ExchangeInstallmentCabinetService exchangeInstallmentCabinetService;
	@Autowired
	ExchangeInstallmentCountDetailService exchangeInstallmentCountDetailService;
	@Autowired
	ExchangeInstallmentCountService exchangeInstallmentCountService;
	@Autowired
	ExchangeInstallmentCustomerService exchangeInstallmentCustomerService;

	@Autowired
	ExchangeInstallmentStationService exchangeInstallmentStationService;

	@SecurityControl(limits = "hdg.ExchangeInstallmentSetting:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentSetting:list");
		model.addAttribute("isActive", ConstEnum.Flag.values());
		model.addAttribute("settingType", ExchangeInstallmentSetting.SettingType.values());

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
			List<ExchangeInstallmentCount> settingId = exchangeInstallmentCountService.findSettingId(entity.getId());
			Map <String,Object> exchangeInstallmentCountDetailMap = new HashMap<String,Object>();
			if(entity.getSettingType()==2){
				for (ExchangeInstallmentCount exchangeInstallmentCount: settingId) {
					List<ExchangeInstallmentCountDetail> countId = exchangeInstallmentCountDetailService.findCountId(exchangeInstallmentCount.getId());
					if(countId!=null){
						exchangeInstallmentCountDetailMap.put(exchangeInstallmentCount.getId().toString(),countId);
					}
				}
			}
			List<ExchangeInstallmentCustomer> exchangeInstallmentCustomerList = exchangeInstallmentCustomerService.findSettingId(entity.getId());
			List<ExchangeInstallmentCabinet> exchangeInstallmentCabinetList = exchangeInstallmentCabinetService.findSettingId(entity.getId());
			List<ExchangeInstallmentStation> exchangeInstallmentStationsList = exchangeInstallmentStationService.findSettingId(entity.getId());
			model.addAttribute("exchangeInstallmentStationsList",exchangeInstallmentStationsList);
			model.addAttribute("exchangeInstallmentCabinetList",exchangeInstallmentCabinetList);
			model.addAttribute("exchangeInstallmentCustomerList",exchangeInstallmentCustomerList);
			model.addAttribute("exchangeInstallmentCountDetailMap",exchangeInstallmentCountDetailMap);
			model.addAttribute("exchangeInstallmentCountList",settingId);
			model.addAttribute("settingType",entity.getSettingType());
			model.addAttribute("entity", entity);
			model.addAttribute("SettingType", ExchangeInstallmentSetting.SettingType.values());
		}
		return "/security/hdg/exchange_installment_setting/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("defaultNum", ExchangeInstallmentSetting.Num.THREE.getValue());
		model.addAttribute("NumEnum", ExchangeInstallmentSetting.Num.values());
		model.addAttribute("SettingTypeSS", ExchangeInstallmentSetting.SettingType.STANDARD_STAGING.getValue());
		model.addAttribute("SettingType", ExchangeInstallmentSetting.SettingType.values());
	}
	@RequestMapping("add_installment_setting.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult add_installment_setting(Model model, String data) throws IOException, ParseException {
		return exchangeInstallmentSettingService.addInstallmentSetting(data);
	}

	@RequestMapping("edit_installment_setting.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult edit_installment_setting(Model model,ExchangeInstallmentSetting exchangeInstallmentSetting){

		return exchangeInstallmentSettingService.updateExchangeInstallmentSetting(exchangeInstallmentSetting);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_price.htm")
	public void selectPrice(Model model, Integer agentId, Integer batteryType) {
		List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findListByBatteryType(batteryType, agentId);
		model.addAttribute("exchangeBatteryForegiftList", exchangeBatteryForegiftList);
		if (exchangeBatteryForegiftList.size() > 0) {
			model.addAttribute("exchangeBatteryForegift", exchangeBatteryForegiftList.get(0));
			List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(exchangeBatteryForegiftList.get(0).getId(), batteryType, agentId);
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
			List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(exchangeBatteryForegiftList.get(0).getId(), batteryType, agentId);
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
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
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
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
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
			List<ExchangeInstallmentCount> settingId = exchangeInstallmentCountService.findSettingId(entity.getId());
			Map <String,Object> exchangeInstallmentCountDetailMap = new HashMap<String,Object>();
			if(entity.getSettingType()==2){
				for (ExchangeInstallmentCount exchangeInstallmentCount: settingId) {
					List<ExchangeInstallmentCountDetail> countId = exchangeInstallmentCountDetailService.findCountId(exchangeInstallmentCount.getId());
					if(countId!=null){
						exchangeInstallmentCountDetailMap.put(exchangeInstallmentCount.getId().toString(),countId);
					}
				}
			}
			List<ExchangeInstallmentCustomer> exchangeInstallmentCustomerList = exchangeInstallmentCustomerService.findSettingId(entity.getId());
			List<ExchangeInstallmentCabinet> exchangeInstallmentCabinetList = exchangeInstallmentCabinetService.findSettingId(entity.getId());
			List<ExchangeInstallmentStation> exchangeInstallmentStationsList = exchangeInstallmentStationService.findSettingId(entity.getId());
			model.addAttribute("exchangeInstallmentStationsList",exchangeInstallmentStationsList);
			model.addAttribute("exchangeInstallmentCabinetList",exchangeInstallmentCabinetList);
			model.addAttribute("exchangeInstallmentCustomerList",exchangeInstallmentCustomerList);
			model.addAttribute("exchangeInstallmentCountDetailMap",exchangeInstallmentCountDetailMap);
			model.addAttribute("exchangeInstallmentCountList",settingId);
			model.addAttribute("settingType",entity.getSettingType());
			model.addAttribute("entity", entity);
			model.addAttribute("SettingType", ExchangeInstallmentSetting.SettingType.values());
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
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findListByForegiftId(foregiftId, batteryType, agentId);
		if (packetPeriodPriceList.size() > 0) {
			return DataResult.successResult(packetPeriodPriceList.get(0));
		} else {
			return ExtResult.failResult("押金信息不存在");
		}
	}
}
