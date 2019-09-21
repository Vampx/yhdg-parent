package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;

import cn.com.yusong.yhdg.agentserver.service.hdg.VipPriceShopService;
import cn.com.yusong.yhdg.agentserver.service.zc.VehicleVipPriceShopService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 租车vip套餐绑定柜子
 */
@Controller
@RequestMapping(value = "/security/zc/vehicle_vip_price_shop")
public class VehicleVipPriceShopController extends SecurityController {

    @Autowired
	VipPriceShopService vipPriceShopService;
	@Autowired
	VehicleVipPriceShopService vehicleVipPriceShopService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vehicle_vip_price_shop.htm")
	public void vehicleVipPriceShop(Model model, Long priceId, Integer agentId) {
		List<VehicleVipPriceShop> vehicleVipPriceShopList = vehicleVipPriceShopService.findListByPriceId(priceId);
		model.addAttribute("vehicleVipPriceShopList", vehicleVipPriceShopList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VehicleVipPriceShop entity) {
		return vehicleVipPriceShopService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vehicleVipPriceShopService.delete(id, priceId);
	}

}
