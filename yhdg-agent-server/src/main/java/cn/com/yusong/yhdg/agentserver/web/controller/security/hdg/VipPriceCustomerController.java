package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.VipPriceCustomerService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip客户
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_price_customer")
public class VipPriceCustomerController extends SecurityController {

    @Autowired
	VipPriceCustomerService vipPriceCustomerService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_customer.htm")
	public void vipPriceCustomer(Model model, Long priceId, Integer agentId) {
		List<VipPriceCustomer> vipPriceCustomerList = vipPriceCustomerService.findListByPriceId(priceId);
		model.addAttribute("vipPriceCustomerList", vipPriceCustomerList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping(value = "add.htm")
	public void add(Model model, Long priceId, Integer agentId) {
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipPriceCustomer entity) {
		return vipPriceCustomerService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipPriceCustomerService.delete(id, priceId);
	}

}
