package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.StationRole;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.StationRoleService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationService;
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
@RequestMapping(value = "/security/basic/station_role")
public class StationRoleController extends SecurityController {
	@Autowired
	StationRoleService stationRoleService;
	@Autowired
	StationService stationService;
	@Autowired
	AgentService agentService;

	@SecurityControl(limits = "basic.StationRole:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) throws Exception {
		model.addAttribute(MENU_CODE_NAME, "basic.StationRole:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(StationRole search) {
		return PageResult.successResult(stationRoleService.findPage(search));
	}

	@ResponseBody
	@ViewModel(ViewModel.JSON_ARRAY)
	@RequestMapping(value = "station_app_tree.htm")
	public void stationAppTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		stationRoleService.stationAppTree(id, response.getOutputStream());
	}

	//门店账户页面根据门店id获取门店角色
	@RequestMapping("find_all.htm")
	@ViewModel(ViewModel.JSON_ARRAY)
	@ResponseBody
	public void findAllUser(String stationId, HttpServletResponse response) throws IOException {
		response.setContentType(ConstEnum.ContentType.JSON.getValue());
		if(stationId != null){
			stationRoleService.tree(stationId == null ? "" : stationId, response.getOutputStream());
		}
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("add.htm")
	public void add(Model model, String stationId, Integer agentId) {
		Station station = stationService.find(stationId);
		Agent agent = agentService.find(agentId);
		model.addAttribute("stationId", stationId);
		model.addAttribute("agentId", agentId);
		model.addAttribute("stationName", station.getStationName());
		model.addAttribute("agentName", agent.getAgentName());
	}

	@ResponseBody
	@RequestMapping(value = "create.htm")
	public ExtResult create(StationRole role, String stationAppOperIds) {
		String[] operStr = StringUtils.split(stationAppOperIds, ",");
		List<String> operList = new ArrayList<String>();
        if (operStr != null) {
			for (String oper : operStr) {
				operList.add(oper);
			}
			role.setPermList(operList);
		}
		role.setCreateTime(new Date());
		return stationRoleService.create(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("edit.htm")
	public String edit(int id, Model model) {
		StationRole role = stationRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}
		Station station = stationService.find(role.getStationId());
		Agent agent = agentService.find(station.getAgentId());
		model.addAttribute("stationId", station.getId());
		model.addAttribute("agentId", agent.getId());
		model.addAttribute("stationName", station.getStationName());
		model.addAttribute("agentName", agent.getAgentName());
		model.addAttribute("entity", role);
		return "/security/basic/station_role/edit";
	}

	@ResponseBody
	@RequestMapping(value = "update.htm")
	public ExtResult update(StationRole role, String stationAppOperIds) {
		String[] operStr = StringUtils.split(stationAppOperIds, ",");
		List<String> operList = new ArrayList<String>();

		for (String oper : operStr) {
			operList.add(oper);
		}
		role.setPermList(operList);
		return stationRoleService.update(role);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view.htm")
	public String view(int id, Model model) {
		StationRole role = stationRoleService.find(id);
		if(role == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		}
		Station station = stationService.find(role.getStationId());
		Agent agent = agentService.find(station.getAgentId());
		model.addAttribute("stationId", station.getId());
		model.addAttribute("agentId", agent.getId());
		model.addAttribute("stationName", station.getStationName());
		model.addAttribute("agentName", agent.getAgentName());
		model.addAttribute("entity", role);
		return "/security/basic/station_role/view";
	}
	@ResponseBody
	@RequestMapping(value = "delete.htm")
	public ExtResult delete(int id) {
		return stationRoleService.delete(id);
	}

	@ResponseBody
	@RequestMapping(value = "batch_delete.htm")
	public ExtResult batchDelete(int[] id) {
		return stationRoleService.batchDelete(id);
	}

}
