package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.zd.VipRentPriceCustomerService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
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
@RequestMapping(value = "/security/zd/vip_rent_price_customer")
public class VipRentPriceCustomerController extends SecurityController {

    @Autowired
	VipRentPriceCustomerService vipRentPriceCustomerService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_customer.htm")
	public void vipPriceCustomer(Model model, Long priceId, Integer agentId) {
		List<VipRentPriceCustomer> vipRentPriceCustomerList = vipRentPriceCustomerService.findListByPriceId(priceId);
		model.addAttribute("vipPriceCustomerList", vipRentPriceCustomerList);
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
	public ExtResult create(VipRentPriceCustomer entity) {
		return vipRentPriceCustomerService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipRentPriceCustomerService.delete(id, priceId);
	}

}
