package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceStation;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceStationService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * vip绑定站点
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_price_station")
public class VipPriceStationController extends SecurityController {

    @Autowired
	VipPriceStationService vipPriceStationService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_station.htm")
	public void vipPriceCabinet(Model model, Long priceId, Integer agentId) {
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "vip_price_station_edit.htm")
	public void vipPriceCabinetEdit(Model model, Long priceId, Integer agentId) {
		List<VipPriceStation> vipPriceStationList = vipPriceStationService.findListByPriceId(priceId);
		model.addAttribute("vipPriceStationList", vipPriceStationList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipPriceStation entity) {
		return vipPriceStationService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipPriceStationService.delete(id, priceId);
	}

}
