package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Role;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.RoleService;
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
@RequestMapping(value = "/security/basic/agent_role")
public class AgentRoleController extends SecurityController {
    @Autowired
    RoleService roleService;
    @Autowired
    AgentService agentService;

    @SecurityControl(limits = "basic.AgentRole:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
       model.addAttribute(MENU_CODE_NAME, "basic.AgentRole:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Role search) {
        return PageResult.successResult(roleService.findAgentPage(search));
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "agent_web_tree.htm")
    public void webTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        roleService.agentWebTree(id, response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "agent_app_tree.htm")
    public void appTree(Integer id, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        roleService.agentAppTree(id, response.getOutputStream());
    }

    @RequestMapping("find_all.htm")
    @ViewModel(ViewModel.JSON_ARRAY)
    @ResponseBody
    public void findAllUser(Integer agentId, HttpServletResponse response) throws IOException {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        if(agentId != null){
            roleService.tree(agentId == null ? 0 : agentId, response.getOutputStream());
        }
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {

    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(Role role, String agentWebOperIds, String agentAppOperIds) {
        String[] webOperStr = StringUtils.split(agentWebOperIds, ",");
        String[] appOperStr = StringUtils.split(agentAppOperIds, ",");
        List<String> operList = new ArrayList<String>();

        for (String oper : webOperStr) {
            operList.add(oper);
        }
        for (String oper : appOperStr) {
            operList.add(oper);
        }
        role.setPermList(operList);
        role.setCreateTime(new Date());
        return roleService.create(role);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        Role role = roleService.find(id);
        if(role == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }else {
            Integer agentId = role.getAgentId();
            if (agentId != null && agentId != 0) {
                role.setAgentName(agentService.find(agentId).getAgentName());
            }
        }

        model.addAttribute("entity", role);
        return "/security/basic/agent_role/edit";
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(Role role, String agentWebOperIds, String agentAppOperIds) {
        String[] webOperStr = StringUtils.split(agentWebOperIds, ",");
        String[] appOperStr = StringUtils.split(agentAppOperIds, ",");
        List<String> operList = new ArrayList<String>();

        for (String oper : webOperStr) {
            operList.add(oper);
        }
        for (String oper : appOperStr) {
            operList.add(oper);
        }
        role.setPermList(operList);
        return roleService.update(role);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int id, Model model) {
        Role role = roleService.find(id);
        if(role == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = role.getAgentId();
            if (agentId != null && agentId != 0) {
                role.setAgentName(agentService.find(agentId).getAgentName());
            }
        }

        model.addAttribute("entity", role);
        return "/security/basic/agent_role/view";
    }
    @ResponseBody
    @RequestMapping(value = "delete.htm")
    public ExtResult delete(int id) {
        return roleService.delete(id);
    }

    @ResponseBody
    @RequestMapping(value = "batch_delete.htm")
    public ExtResult batchDelete(int[] id) {
        return roleService.batchDelete(id);
    }

}
