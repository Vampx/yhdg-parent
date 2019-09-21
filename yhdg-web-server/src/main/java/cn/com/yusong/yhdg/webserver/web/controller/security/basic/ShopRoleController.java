package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.domain.basic.ShopRole;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.RoleService;
import cn.com.yusong.yhdg.webserver.service.basic.ShopRoleService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
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
@RequestMapping(value = "/security/basic/shop_role")
public class ShopRoleController extends SecurityController {
	@Autowired
	ShopRoleService shopRoleService;
	@Autowired
	ShopService shopService;

	@SecurityControl(limits = "basic.ShopRole:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) throws Exception {
		model.addAttribute(MENU_CODE_NAME, "basic.ShopRole:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(ShopRole search) {
		return PageResult.successResult(shopRoleService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "shop_app_tree.htm")
	public void shopAppTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		shopRoleService.shopAppTree(id, response.getOutputStream());
	}

	//门店账户页面根据门店id获取门店角色
	@RequestMapping("find_all.htm")
	@ViewModel(ViewModel.JSON_ARRAY)
	@ResponseBody
	public void findAllUser(String shopId, HttpServletResponse response) throws IOException {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		if(shopId != null){
			shopRoleService.tree(shopId == null ? "" : shopId, response.getOutputStream());
		}
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("add.htm")
	public void add(Model model) {

	}

	@ResponseBody
	@RequestMapping(value = "create.htm")
	public ExtResult create(ShopRole role, String shopAppOperIds) {
		String[] operStr = StringUtils.split(shopAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		role.setCreateTime(new Date());
		return shopRoleService.create(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("edit.htm")
	public String edit(int id, Model model) {
		ShopRole role = shopRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}
		model.addAttribute("entity", role);
		return "/security/basic/shop_role/edit";
	}

	@ResponseBody
	@RequestMapping(value = "update.htm")
	public ExtResult update(ShopRole role, String shopAppOperIds) {
		String[] operStr = StringUtils.split(shopAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		return shopRoleService.update(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view.htm")
	public String view(int id, Model model) {
		ShopRole role = shopRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}

		model.addAttribute("entity", role);
		return "/security/basic/shop_role/view";
	}
	@ResponseBody
	@RequestMapping(value = "delete.htm")
	public ExtResult delete(int id) {
		return shopRoleService.delete(id);
	}

	@ResponseBody
	@RequestMapping(value = "batch_delete.htm")
	public ExtResult batchDelete(int[] id) {
		return shopRoleService.batchDelete(id);
	}

}
