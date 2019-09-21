package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.VipPriceShopService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
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
@RequestMapping(value = "/security/hdg/vip_price_shop")
public class VipPriceShopController extends SecurityController {

    @Autowired
	VipPriceShopService vipPriceShopService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_shop.htm")
	public void vipPriceCabinet(Model model, Long priceId, Integer agentId) {
		List<VipPriceShop> vipPriceShopList = vipPriceShopService.findListByPriceId(priceId);
		model.addAttribute("vipPriceShopList", vipPriceShopList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipPriceShop entity) {
		return vipPriceShopService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipPriceShopService.delete(id, priceId);
	}

}
