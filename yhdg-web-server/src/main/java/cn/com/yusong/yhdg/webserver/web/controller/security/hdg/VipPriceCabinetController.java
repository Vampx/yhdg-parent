package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceCabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceCustomerService;
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
@RequestMapping(value = "/security/hdg/vip_price_cabinet")
public class VipPriceCabinetController extends SecurityController {

    @Autowired
	VipPriceCabinetService vipPriceCabinetService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_cabinet.htm")
	public void vipPriceCabinet(Model model, Long priceId, Integer agentId) {
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "vip_price_cabinet_edit.htm")
	public void vipPriceCabinetEdit(Model model, Long priceId, Integer agentId) {
		List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetService.findListByPriceId(priceId);
		model.addAttribute("vipPriceCabinetList", vipPriceCabinetList);
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(VipPriceCabinet entity) {
		return vipPriceCabinetService.create(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long id, Long priceId) {
		return vipPriceCabinetService.delete(id, priceId);
	}

}
