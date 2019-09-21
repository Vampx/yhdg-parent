package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryBarcode;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryBarcodeMapper;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 电池检验
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_check")
public class BatteryCheckController extends SecurityController {
	@Autowired
	BatteryService batteryService;
	@Autowired
	BatteryBarcodeMapper batteryBarcodeMapper;

//	@SecurityControl(limits = OperCodeConst.CODE_1_5_5_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_05_05.getValue());
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(Battery search) {
		return PageResult.successResult(batteryService.findCheckedPage(search));
	}

	@RequestMapping(value = "exchange_qrcode.htm")
	public String exchangeQrcode(Model model, String id) {
		Battery entity = batteryService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_check/exchange_qrcode";
	}

	@RequestMapping(value = "edit_code.htm")
	public String updateCode(Model model, String id) {
		Battery entity = batteryService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_check/edit_code";
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(String id) {
		return batteryService.delete(id);
	}


	@RequestMapping("update_qrcode.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult updateQrcode(Battery entity) {
		return batteryService.updateQrcode(entity.getId(), entity.getQrcode(), entity.getCode());
	}

	@RequestMapping("update_code.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult updateCode(Battery entity) {
		return batteryService.updateCode(entity.getId(), entity.getCode());
	}

	@RequestMapping("find_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findBattery(String id) {
		Battery battery = batteryService.find(id);
		int cellCount = battery.getCellCount();
		return DataResult.successResult(cellCount);
	}

	@RequestMapping("check_bind_param.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult checkBindParam(String code, String shellCode) {
		return batteryService.checkBindParam(code, shellCode);
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("AppearanceEnum", Battery.Appearance.values());
	}

	@RequestMapping(value = "show_bound.htm")
	public void showBound(Model model, String batteryId) {
		Battery entity = batteryService.find(batteryId);
		model.addAttribute("entity", entity);
		model.addAttribute("AppearanceEnum", Battery.Appearance.values());
		BatteryBarcode batteryBarcode = batteryBarcodeMapper.findByBarcode(entity.getShellCode());
		if (batteryBarcode != null) {
			model.addAttribute("batteryFormatId", batteryBarcode.getBatteryFormatId());
		}
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(Battery entity) {
		return batteryService.createCheckedBattery(entity);
	}

	@RequestMapping("check_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult checkBattery(Battery entity) {
		return batteryService.checkBattery(entity);
	}
}
