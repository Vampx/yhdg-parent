package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceShopService;
import cn.com.yusong.yhdg.webserver.service.zd.VipRentPriceShopService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip绑定柜子
 */
@Controller
@RequestMapping(value = "/security/zd/vip_rent_price_shop")
public class VipRentPriceShopController extends SecurityController {

    @Autowired
	VipRentPriceShopService vipRentPriceShopService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_shop.htm")
	public void vipPriceCabinet(Model model, Long priceId, Integer agentId) {
		List<VipRentPriceShop> vipPriceShopList = vipRentPriceShopService.findListByPriceId(priceId);
		model.addAttribute("vipPriceShopList", vipPriceShopList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipRentPriceShop entity) {
		return vipRentPriceShopService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipRentPriceShopService.delete(id, priceId);
	}

}
