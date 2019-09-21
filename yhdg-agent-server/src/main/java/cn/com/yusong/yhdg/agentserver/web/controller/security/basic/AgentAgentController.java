package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/agent_agent")
public class AgentAgentController extends SecurityController {

    @Autowired
    AgentService agentService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response, HttpSession httpSession) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        agentService.tree(dummy, agentId, response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "top_agent_list.htm")
    public List topAgentList() throws Exception {
        return  agentService.topAgentList();
    }

    @SecurityControl(limits = "basic.Agent:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession httpSession, Integer parentId) {
        Integer agentId = getSessionUser(httpSession).getAgentId();
        if (agentId != 0) {
            model.addAttribute("agentId", agentId);
        }
        model.addAttribute("parentId", parentId);
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        model.addAttribute("gradeEnum", Agent.Grade.values());
        model.addAttribute(MENU_CODE_NAME, "basic.Agent:list");
    }

    @RequestMapping("page_tree.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageTree(Agent search, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        search.setId(sessionUser.getAgentId());
        return PageResult.successResult(agentService.findPageTree(search));
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Agent search, HttpSession httpSession) {
        if (search.getId() == null) {
            SessionUser sessionUser = getSessionUser(httpSession);
            search.setId(sessionUser.getAgentId());
        }
        return PageResult.successResult(agentService.findPage(search));
    }

    @RequestMapping("childPage.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public List<Agent> childPage(Integer parentId) {
        return agentService.childPage(parentId);
    }

    @RequestMapping(value = "add.htm")
    public void add(Integer agentId, Model model) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        model.addAttribute("gradeEnum", Agent.Grade.values());
    }

    @RequestMapping(value = "add_basic_info.htm")
    public void addBasicInfo(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("gradeEnum", Agent.Grade.values());
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
    }

    @RequestMapping(value = "edit.htm")
    public void edit(Model model, int id, Integer agentId) {
        model.addAttribute("id", id);
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping(value = "edit_basic_info.htm")
    public String editBasicInfo(Model model, int id, Integer agentId) {
        Agent entity = agentService.find(id);
        AgentSystemConfig agentSystemConfig = agentSystemConfigService.find("wxmp.app.id", id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("agentId", agentId);
            model.addAttribute("gradeEnum", Agent.Grade.values());
            model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
            model.addAttribute("entity", entity);
            if (agentSystemConfig != null) {
                model.addAttribute("configValue", agentSystemConfig.getConfigValue());
            }else {
                model.addAttribute("configValue", "");
            }
        }
        return "/security/basic/agent_agent/edit_basic_info";
    }

    @RequestMapping(value = "edit_payee.htm")
    public void editPayee(Model model, int id, Integer agentId) {
        Agent entity = agentService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("gradeEnum", Agent.Grade.values());
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        model.addAttribute("entity", entity);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Agent entity) {
        if (entity.getProvinceId() == null) {
            return ExtResult.failResult("请选择运营商所属省份城市");
        }
        agentService.insert(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Agent entity) {
        agentService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return agentService.delete(id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        model.addAttribute("id", id);
        return "/security/basic/agent_agent/view";
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_basic_info.htm")
    public String viewBasicInfo(Model model, int id) {
        Agent entity = agentService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("gradeEnum", Agent.Grade.values());
            model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        }
        return "/security/basic/agent_agent/view_basic_info";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_in_out_money.htm")
    public String viewInOutMoney(Model model, int id) {
        model.addAttribute("id", id);
        return "/security/basic/agent_agent/view_in_out_money";
    }

    @ResponseBody
    @RequestMapping("update_order_num.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateOrderNum(int id, int orderNum, HttpSession httpSession) {
        agentService.updateOrderNum(id, orderNum);
        return ExtResult.successResult();
    }

}
