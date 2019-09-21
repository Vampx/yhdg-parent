package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/agent")
public class AgentController extends SecurityController {

    @Autowired
    AgentService agentService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        agentService.tree(dummy, agentId, response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "agent_list.htm")
    public List AgentList() {
        Agent none = new Agent();
        none.setId(0);
        none.setAgentName("无");

        List<Agent> list = new ArrayList<Agent>();
        list.add(none);
        list.addAll(agentService.findAll());

        return list;
    }

    @SecurityControl(limits = "basic.Agent:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpServletRequest request) {
        model.addAttribute(MENU_CODE_NAME, "basic.Agent:list");
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        model.addAttribute("gradeEnum", Agent.Grade.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Agent search) {
        return PageResult.successResult(agentService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
        model.addAttribute("gradeEnum", Agent.Grade.values());
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, int id) {
        Agent entity = agentService.find(id);
        AgentSystemConfig agentSystemConfig = agentSystemConfigService.find("wxmp.app.id", id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
            model.addAttribute("gradeEnum", Agent.Grade.values());
            model.addAttribute("entity", entity);
            if (agentSystemConfig != null) {
                model.addAttribute("configValue", agentSystemConfig.getConfigValue());
            }else {
                model.addAttribute("configValue", "");
            }
        }
        return "/security/basic/agent/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Agent entity) {
        if (entity.getProvinceId() == null) {
            return ExtResult.failResult("请选择运营商所属省份城市");
        }
        agentService.insert(entity);
        return DataResult.successResult(entity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Agent entity) {
        agentService.update(entity);
        return ExtResult.successResult();
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(int id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        agentService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, CodecUtils.password(payPassword));
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
        return "/security/basic/agent/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_basic_info.htm")
    public String viewBasicInfo(Model model, int id) {
        Agent entity = agentService.find(id);
        AgentSystemConfig agentSystemConfig = agentSystemConfigService.find("wxmp.app.id", id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("gradeEnum", Agent.Grade.values());
            model.addAttribute("BalanceStatusEnum", Agent.BalanceStatus.values());
            if (agentSystemConfig != null) {
                model.addAttribute("configValue", agentSystemConfig.getConfigValue());
            }else {
                model.addAttribute("configValue", "");
            }
        }
        return "/security/basic/agent/view_basic_info";
    }

    @ResponseBody
    @RequestMapping("update_order_num.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateOrderNum(int id, int orderNum, HttpSession httpSession) {
        agentService.updateOrderNum(id, orderNum);
        return ExtResult.successResult();
    }

}
