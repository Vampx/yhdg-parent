package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.SystemBatteryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  系统电池类型
 */
@Controller
@RequestMapping(value = "/security/basic/system_battery_type")
public class SystemBatteryTypeController extends SecurityController {
	@Autowired
	SystemBatteryTypeService systemBatteryTypeService;

//	@SecurityControl(limits = OperCodeConst.CODE_1_4_1_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_04_01.getValue());
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_battery_type.htm")
	public void selectBatteryType() {

	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(SystemBatteryType search) {
		return PageResult.successResult(systemBatteryTypeService.findPage(search));
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Integer id) {
		SystemBatteryType entity = systemBatteryTypeService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/system_battery_type/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(SystemBatteryType entity) {
		return systemBatteryTypeService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Integer id) {
		SystemBatteryType entity = systemBatteryTypeService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/basic/system_battery_type/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(SystemBatteryType entity) {
		return systemBatteryTypeService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Integer id) {
		return systemBatteryTypeService.delete(id);
	}

}
