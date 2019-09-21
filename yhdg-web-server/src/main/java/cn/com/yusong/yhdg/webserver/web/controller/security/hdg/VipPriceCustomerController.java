package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCustomer;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPacketPeriodPriceService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceCustomerService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * vip客户
 */
@Controller
@RequestMapping(value = "/security/hdg/vip_price_customer")
public class VipPriceCustomerController extends SecurityController {

    @Autowired
	VipPriceCustomerService vipPriceCustomerService;
	@Autowired
	CustomerService customerService;
	@Autowired
	VipPriceService vipPriceService;

	@RequestMapping(value = "index.htm")
    public void index(Model model) {
    }

	@RequestMapping(value = "vip_price_customer.htm")
	public void vipPriceCustomer(Model model, Long priceId, Integer agentId) {
		model.addAttribute("priceId", priceId);
		model.addAttribute("agentId", agentId);
	}

	@RequestMapping(value = "vip_price_customer_edit.htm")
	public void vipPriceCustomerEdit(Model model, Long priceId, Integer agentId) {
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

	@RequestMapping("add_vip_customer_mobile")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult addVipCustomerMobile(Model model, String mobile, Integer agentId, Long priceId){
		int insert = 0;
		if(StringUtils.isEmpty(mobile)){
			return ExtResult.failResult("手机号不能为空");
		}
		Customer byMobile = customerService.findByMobile(mobile);

		VipPriceCustomer vipMobile = vipPriceCustomerService.findByAgentIdAndMobile(agentId, mobile);
		if (vipMobile != null) {
			return ExtResult.failResult("该骑手手机号已存在");
		}

		if(priceId != null){
			VipPriceCustomer vipPriceCustomer = new VipPriceCustomer();
			vipPriceCustomer.setPriceId(priceId);
			vipPriceCustomer.setMobile(mobile);
			vipPriceCustomer.setCreateTime(new Date());
			vipPriceCustomerService.insert(vipPriceCustomer);
			List<VipPriceCustomer> customerList = vipPriceCustomerService.findListByPriceId(vipPriceCustomer.getPriceId());
			vipPriceService.updateCustomerCount(vipPriceCustomer.getPriceId(), customerList.size());
		}
		if(insert==0 && priceId != null){
			return ExtResult.failResult("绑定骑手失败！");
		}else{
			return DataResult.successResult(byMobile);
		}

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
