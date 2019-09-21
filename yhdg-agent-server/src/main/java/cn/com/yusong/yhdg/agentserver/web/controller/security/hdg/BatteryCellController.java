package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryCellBarcodeService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryCellService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryFormatService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  电芯检验
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_cell")
public class BatteryCellController extends SecurityController {
	@Autowired
	BatteryCellService batteryCellService;
	@Autowired
	BatteryCellBarcodeService batteryCellBarcodeService;
	@Autowired
	BatteryFormatService batteryFormatService;

//	@SecurityControl(limits = OperCodeConst.CODE_1_5_3_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_05_03.getValue());
		model.addAttribute("AppearanceEnum", BatteryCell.Appearance.values());
	}

	@RequestMapping(value = "find_cell.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult findCell(String barcode) {
		return batteryCellService.findByBarcode(barcode);
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(BatteryCell search) {
		return PageResult.successResult(batteryCellService.findPage(search));
	}

	@RequestMapping("bound_page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult boundPage(BatteryCell search) {
		return PageResult.successResult(batteryCellService.findBoundPage(search));
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		BatteryCell entity = batteryCellService.find(id);
		String barcode = entity.getBarcode();
		ExtResult extResult = batteryCellBarcodeService.findByBarcode(barcode, "");
		if (extResult.isSuccess()) {
			DataResult result = (DataResult) batteryCellBarcodeService.findByBarcode(barcode, "");
			BatteryCellBarcode batteryCellBarcode = (BatteryCellBarcode) result.getData();
			if (batteryCellBarcode != null) {
				model.addAttribute("cellFormatId", batteryCellBarcode.getCellFormatId());
			}
		}

		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("AppearanceEnum", BatteryCell.Appearance.values());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_cell/view";
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		BatteryCell entity = batteryCellService.find(id);
		String barcode = entity.getBarcode();
		ExtResult extResult = batteryCellBarcodeService.findByBarcode(barcode, "");
		if (extResult.isSuccess()) {
			DataResult result = (DataResult) batteryCellBarcodeService.findByBarcode(barcode, "");
			BatteryCellBarcode batteryCellBarcode = (BatteryCellBarcode) result.getData();
			if (batteryCellBarcode != null) {
				model.addAttribute("cellFormatId", batteryCellBarcode.getCellFormatId());
			}
		}

		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("AppearanceEnum", BatteryCell.Appearance.values());
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_cell/edit";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
		model.addAttribute("AppearanceEnum", BatteryCell.Appearance.values());
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(BatteryCell entity, HttpSession httpSession) {
		SessionUser sessionUser = getSessionUser(httpSession);
		String username = sessionUser.getUsername();
		entity.setOperator(username);
		return batteryCellService.create(entity);
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(BatteryCell entity) {
		return batteryCellService.update(entity);
	}

	@RequestMapping("unbind.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult unbind(Long id, String batteryId) {
		return batteryCellService.unbind(id, batteryId);
	}

	@RequestMapping(value = "bind.htm")
	public void bind(Model model, Long batteryFormatId, String batteryId, String cellMfr, String cellModel, String shellCode, String code, Integer appearance) {
		model.addAttribute("batteryFormatId", batteryFormatId);
		BatteryFormat batteryFormat = batteryFormatService.find(batteryFormatId);
		model.addAttribute("formatCellCount", batteryFormat.getCellCount());
		model.addAttribute("batteryId", batteryId);
		model.addAttribute("cellMfr", cellMfr);
		model.addAttribute("cellModel", cellModel);
		model.addAttribute("shellCode", shellCode);
		model.addAttribute("code", code);
		model.addAttribute("appearance", appearance);
	}

	@RequestMapping("bind_battery.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult bindBattery(Long id, String batteryId, Integer formatId) {
		return batteryCellService.bindBattery(id, batteryId, formatId);
	}
}
