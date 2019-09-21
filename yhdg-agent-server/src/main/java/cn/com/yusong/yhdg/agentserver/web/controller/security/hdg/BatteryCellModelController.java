package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryCellModelService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  电芯型号管理
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_cell_model")
public class BatteryCellModelController extends SecurityController {
	@Autowired
	BatteryCellModelService batteryCellModelService;

//	@SecurityControl(limits = OperCodeConst.CODE_1_5_1_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_05_01.getValue());
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(BatteryCellModel search) {
		return PageResult.successResult(batteryCellModelService.findPage(search));
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		BatteryCellModel entity = batteryCellModelService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_cell_model/view";
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(BatteryCellModel entity, HttpSession httpSession) {
		SessionUser sessionUser = getSessionUser(httpSession);
		String username = sessionUser.getUsername();
		entity.setOperator(username);
		return batteryCellModelService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		BatteryCellModel entity = batteryCellModelService.find(id);
		if(entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/battery_cell_model/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(BatteryCellModel entity) {
		return batteryCellModelService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id) {
		return batteryCellModelService.delete(id);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "select_battery_cell_model.htm")
	public void selectBatteryCellModel() {

	}
}
