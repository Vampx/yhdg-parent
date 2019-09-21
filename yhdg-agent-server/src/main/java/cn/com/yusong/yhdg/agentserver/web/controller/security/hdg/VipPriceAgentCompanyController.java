package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.VipPriceAgentCompanyService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceAgentCompany;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip绑定运营公司
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_price_agent_company")
public class VipPriceAgentCompanyController extends SecurityController {

    @Autowired
	VipPriceAgentCompanyService vipPriceAgentCompanyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_agent_company.htm")
	public void vipPriceAgentCompany(Model model, Long priceId, Integer agentId) {
		List<VipPriceAgentCompany> vipPriceAgentCompanyList = vipPriceAgentCompanyService.findListByPriceId(priceId);
		model.addAttribute("vipPriceAgentCompanyList", vipPriceAgentCompanyList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipPriceAgentCompany entity) {
		return vipPriceAgentCompanyService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipPriceAgentCompanyService.delete(id, priceId);
	}

}
