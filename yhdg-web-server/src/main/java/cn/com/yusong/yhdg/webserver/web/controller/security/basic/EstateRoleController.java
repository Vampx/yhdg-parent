package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.EstateRole;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.EstateRoleService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/estate_role")
public class EstateRoleController extends SecurityController {
	@Autowired
	EstateRoleService estateRoleService;
	@Autowired
	ShopService shopService;

	@SecurityControl(limits = "basic.EstateRole:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) throws Exception {
		model.addAttribute(MENU_CODE_NAME, "basic.EstateRole:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(EstateRole search) {
		return PageResult.successResult(estateRoleService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "estate_app_tree.htm")
	public void estateAppTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		estateRoleService.estateAppTree(id, response.getOutputStream());
	}

	//门店账户页面根据门店id获取门店角色
	@RequestMapping("find_all.htm")
	@ViewModel(ViewModel.JSON_ARRAY)
	@ResponseBody
	public void findAllUser(String estateId, HttpServletResponse response) throws IOException {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		if(estateId != null){
			estateRoleService.tree(estateId == null ? "" : estateId, response.getOutputStream());
		}
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("add.htm")
	public void add(Model model) {

	}

	@ResponseBody
	@RequestMapping(value = "create.htm")
	public ExtResult create(EstateRole role, String estateAppOperIds) {
		String[] operStr = StringUtils.split(estateAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		role.setCreateTime(new Date());
		return estateRoleService.create(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("edit.htm")
	public String edit(int id, Model model) {
		EstateRole role = estateRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}
		model.addAttribute("entity", role);
		return "/security/basic/estate_role/edit";
	}

	@ResponseBody
	@RequestMapping(value = "update.htm")
	public ExtResult update(EstateRole role, String estateAppOperIds) {
		String[] operStr = StringUtils.split(estateAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		return estateRoleService.update(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view.htm")
	public String view(int id, Model model) {
		EstateRole role = estateRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}

		model.addAttribute("entity", role);
		return "/security/basic/estate_role/view";
	}
	@ResponseBody
	@RequestMapping(value = "delete.htm")
	public ExtResult delete(int id) {
		return estateRoleService.delete(id);
	}

	@ResponseBody
	@RequestMapping(value = "batch_delete.htm")
	public ExtResult batchDelete(int[] id) {
		return estateRoleService.batchDelete(id);
	}

}
