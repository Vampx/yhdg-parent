package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping(value = "/security/basic/agent_company")
public class AgentCompanyController extends SecurityController {
    @Autowired
    AgentCompanyService agentCompanyService;

    @SecurityControl(limits = "basic.AgentCompany:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentCompany:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        agentCompanyService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentCompany search) {
        return PageResult.successResult(agentCompanyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vip_price_agent_company_page.htm")
    public void vipPriceAgentCompanyPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vip_rent_price_agent_company_page.htm")
    public void vipRentPriceAgentCompanyPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        String id = agentCompanyService.findMaxId();
        model.addAttribute("id", id);
        model.addAttribute("activeStatusEnum", AgentCompany.ActiveStatus.values());
    }

    @RequestMapping(value = "bind_customer.htm")
    public String bindCustomer(Model model, String id, Integer agentId) {
        model.addAttribute("id", id);
        model.addAttribute("agentId", agentId);
        return "/security/basic/agent_company/edit_cabinet";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(AgentCompany entity) {
        return agentCompanyService.insert(entity);
    }

    @RequestMapping(value = "add_location.htm")
    public String addLocation(Model model) {
        model.addAttribute("lng", Constant.LNG);
        model.addAttribute("lat", Constant.LAT);
        return "/security/basic/agent_company/add_location";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        AgentCompany entity = agentCompanyService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_company/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, String id) {
        AgentCompany entity = agentCompanyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", AgentCompany.ActiveStatus.values());
        }
        return "/security/basic/agent_company/edit_basic";
    }

    @RequestMapping(value = "edit_payee.htm")
    public void editPayee(Model model, String id, Integer agentId) {
        AgentCompany entity = agentCompanyService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        agentCompanyService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, CodecUtils.password(payPassword));
        return ExtResult.successResult();
    }

    @RequestMapping(value = "edit_ratio.htm")
    public String editShop(Model model, String id, Integer agentId) {
        AgentCompany entity = agentCompanyService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/basic/agent_company/edit_ratio";
    }

    @RequestMapping("update_ratio.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRatio(String id, Integer companyRatio, Integer keepShopRatio, Integer companyFixedMoney, Integer ratioBaseMoney) {
        ExtResult result = agentCompanyService.updateRatio(id, companyRatio, keepShopRatio, companyFixedMoney, ratioBaseMoney);
        return result;
    }

    @RequestMapping(value = "edit_location.htm")
    public String editLocation(Model model, String id) {
        AgentCompany entity = agentCompanyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_company/edit_location";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(AgentCompany entity) {
        return agentCompanyService.update(entity);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        AgentCompany entity = agentCompanyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/agent_company/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        AgentCompany entity = agentCompanyService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", AgentCompany.ActiveStatus.values());
        }
        return "/security/basic/agent_company/view_basic";
    }

    @RequestMapping(value = "view_payee.htm")
    public String viewPayee(Model model, String id, Integer agentId) {
        AgentCompany entity = agentCompanyService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
        return "/security/basic/agent_company/view_payee";
    }

    @RequestMapping(value = "view_ratio.htm")
    public String viewShop(Model model, String id, Integer agentId) {
        AgentCompany entity = agentCompanyService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/basic/agent_company/view_ratio";
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(AgentCompany entity) {
        return agentCompanyService.updateNewLocation(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return agentCompanyService.delete(id);
    }

}
