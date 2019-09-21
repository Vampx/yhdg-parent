package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceCustomerService;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleVipPriceCustomerService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 租车vip客户
 */
@Controller
@RequestMapping(value = "/security/zc/vehicle_vip_price_customer")
public class VehicleVipPriceCustomerController extends SecurityController {

    @Autowired
	VehicleVipPriceCustomerService vehicleVipPriceCustomerService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vehicle_vip_price_customer.htm")
	public void vipPriceCustomer(Model model, Long priceId, Integer agentId) {
		List<VehicleVipPriceCustomer> vehicleVipPriceCustomerList = vehicleVipPriceCustomerService.findListByPriceId(priceId);
		model.addAttribute("vehicleVipPriceCustomerList", vehicleVipPriceCustomerList);
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
	public ExtResult create(VehicleVipPriceCustomer entity) {
		return vehicleVipPriceCustomerService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vehicleVipPriceCustomerService.delete(id, priceId);
	}

}
