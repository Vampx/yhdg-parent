package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryBarcodeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryFormatMapper;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.service.hdg.UnregisterBatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 电池检验
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_check")
public class BatteryCheckController extends SecurityController {
	@Autowired
	BatteryService batteryService;
	@Autowired
	UnregisterBatteryService unregisterBatteryService;
	@Autowired
	BatteryBarcodeMapper batteryBarcodeMapper;

	@SecurityControl(limits = "hdg.BatteryCheck:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "hdg.BatteryCheck:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(UnregisterBattery search) {
		return PageResult.successResult(unregisterBatteryService.findPage(search));
	}

	@RequestMapping(value = "exchange_qrcode.htm")
	public String exchangeQrcode(Model model, String id) {
		UnregisterBattery entity = unregisterBatteryService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_check/exchange_qrcode";
	}

	@RequestMapping(value = "edit_code.htm")
	public String updateCode(Model model, String id) {
		UnregisterBattery entity = unregisterBatteryService.find(id);
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
		return unregisterBatteryService.delete(id);
	}

	@RequestMapping("update_qrcode.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult updateQrcode(UnregisterBattery entity) {
		return unregisterBatteryService.updateQrcode(entity.getId(), entity.getQrcode());
	}

	@RequestMapping("update_code.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult updateCode(UnregisterBattery entity) {
		return unregisterBatteryService.updateCode(entity.getId(), entity.getCode());
	}

	@RequestMapping("find_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findBattery(String id) {
		UnregisterBattery unregisterBattery = unregisterBatteryService.find(id);
		int cellCount = unregisterBattery.getCellCount();
		return DataResult.successResult(cellCount);
	}

	@RequestMapping("is_sync_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult isSyncBattery(String id) {
		Battery battery = batteryService.find(id);
		if (battery != null && StringUtils.isNotEmpty(battery.getShellCode())) {
			return ExtResult.successResult();
		}
		return ExtResult.failResult("");
	}

	@RequestMapping("find_battery_by_barcode.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findBatteryByBarcode(String shellCode) {
		return unregisterBatteryService.findByShellCode(shellCode);
	}

	@RequestMapping("check_bind_param.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult checkBindParam(String code, String shellCode) {
		return unregisterBatteryService.checkBindParam(code, shellCode);
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("AppearanceEnum", UnregisterBattery.Appearance.values());
	}

	@RequestMapping(value = "show_bound.htm")
	public void showBound(Model model, String batteryId, Integer editFlag) {
		UnregisterBattery entity = unregisterBatteryService.find(batteryId);
		model.addAttribute("entity", entity);
		model.addAttribute("AppearanceEnum", UnregisterBattery.Appearance.values());
		BatteryBarcode batteryBarcode = batteryBarcodeMapper.findByBarcode(entity.getShellCode());
		if (batteryBarcode != null) {
			model.addAttribute("batteryFormatId", batteryBarcode.getBatteryFormatId());
		}
		model.addAttribute("editFlag", editFlag);
	}

	@RequestMapping(value = "edit_cell_bind.htm")
	public void editCellBind(Model model, String batteryId) {
		UnregisterBattery entity = unregisterBatteryService.find(batteryId);
		model.addAttribute("entity", entity);
	}

	@RequestMapping("update_cell_bind.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult updateCellBind(String id, String shellCode, String newShellCode) {
		return unregisterBatteryService.updateCellBind(id, shellCode, newShellCode);
	}

	@RequestMapping("clear_code.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult clearCode(String id) {
		return unregisterBatteryService.clearCode(id);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(UnregisterBattery entity) {
		return unregisterBatteryService.createCheckedBattery(entity);
	}

	@RequestMapping("check_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult checkBattery(UnregisterBattery entity) {
		return unregisterBatteryService.checkBattery(entity);
	}
}
