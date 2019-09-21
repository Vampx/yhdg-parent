package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyRole;
import cn.com.yusong.yhdg.common.domain.basic.ShopRole;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentCompanyRoleService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.webserver.service.basic.ShopRoleService;
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
@RequestMapping(value = "/security/basic/agent_company_role")
public class AgentCompanyRoleController extends SecurityController {
	@Autowired
	AgentCompanyRoleService agentCompanyRoleService;
	@Autowired
	AgentCompanyService agentCompanyService;

	@SecurityControl(limits = "basic.AgentCompanyRole:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) throws Exception {
		model.addAttribute(MENU_CODE_NAME, "basic.AgentCompanyRole:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(AgentCompanyRole search) {
		return PageResult.successResult(agentCompanyRoleService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "agent_company_app_tree.htm")
	public void shopAppTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		agentCompanyRoleService.agentCompanyAppTree(id, response.getOutputStream());
	}

	//运营公司账户页面根据运营公司id获取运营公司角色
	@RequestMapping("find_all.htm")
	@ViewModel(ViewModel.JSON_ARRAY)
	@ResponseBody
	public void findAllUser(String agentCompanyId, HttpServletResponse response) throws IOException {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		if(agentCompanyId != null){
			agentCompanyRoleService.tree(agentCompanyId == null ? "" : agentCompanyId, response.getOutputStream());
		}
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("add.htm")
	public void add(Model model) {

	}

	@ResponseBody
	@RequestMapping(value = "create.htm")
	public ExtResult create(AgentCompanyRole role, String agentCompanyAppOperIds) {
		String[] operStr = StringUtils.split(agentCompanyAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		role.setCreateTime(new Date());
		return agentCompanyRoleService.create(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("edit.htm")
	public String edit(int id, Model model) {
		AgentCompanyRole role = agentCompanyRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}
		model.addAttribute("entity", role);
		return "/security/basic/agent_company_role/edit";
	}

	@ResponseBody
	@RequestMapping(value = "update.htm")
	public ExtResult update(AgentCompanyRole role, String agentCompanyAppOperIds) {
		String[] operStr = StringUtils.split(agentCompanyAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		return agentCompanyRoleService.update(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view.htm")
	public String view(int id, Model model) {
		AgentCompanyRole role = agentCompanyRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}

		model.addAttribute("entity", role);
		return "/security/basic/agent_company_role/view";
	}
	@ResponseBody
	@RequestMapping(value = "delete.htm")
	public ExtResult delete(int id) {
		return agentCompanyRoleService.delete(id);
	}

	@ResponseBody
	@RequestMapping(value = "batch_delete.htm")
	public ExtResult batchDelete(int[] id) {
		return agentCompanyRoleService.batchDelete(id);
	}

}
