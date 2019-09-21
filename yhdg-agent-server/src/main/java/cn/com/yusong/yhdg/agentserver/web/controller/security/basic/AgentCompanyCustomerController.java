package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/agent_company_customer")
public class AgentCompanyCustomerController extends SecurityController {
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;
    @Autowired
    AgentCompanyService agentCompanyService;

    @SecurityControl(limits = "basic.AgentCompanyCustomer:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.AgentCompanyCustomer:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AgentCompanyCustomer search) {
        return PageResult.successResult(agentCompanyCustomerService.findPage(search));
    }

    @RequestMapping("unbind_company_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Customer search) {
        return PageResult.successResult(agentCompanyCustomerService.findUnbindCompanyPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(AgentCompanyCustomer entity) {
        return agentCompanyCustomerService.insert(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String agentCompanyId, Long customerId) {
        return agentCompanyCustomerService.delete(agentCompanyId, customerId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "unbind_company.htm")
    public void unbindCompany(Model model, String agentCompanyId, Integer agentId) {
        model.addAttribute("agentCompanyId", agentCompanyId);
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "bind_customer.htm")
    public void bindCustomer(Model model, String agentCompanyId, Integer agentId, Integer editFlag) {
        model.addAttribute("agentCompanyId", agentCompanyId);
        model.addAttribute("agentId", agentId);
        model.addAttribute("editFlag", editFlag);
    }

    @RequestMapping("batch_bind_company.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchBindCompany(Long[] customerIdList, String agentCompanyId) {
        AgentCompany agentCompany = agentCompanyService.find(agentCompanyId);
        int count = 0;
        for (Long e : customerIdList) {
            count += agentCompanyCustomerService.bindCustomer(e, agentCompanyId, agentCompany.getAgentId(), agentCompany.getAgentName());
        }
        return ExtResult.successResult(String.format("成功绑定%d个骑手", count));
    }

}
