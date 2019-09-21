package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import cn.com.yusong.yhdg.common.domain.hdg.FaultFeedback;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeWhiteListService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 换电白名单
 */
@Controller
@RequestMapping(value = "/security/hdg/exchange_whitelist")
public class ExchangeWhiteListController extends SecurityController {
	@Autowired
	ExchangeWhiteListService exchangeWhiteListService;

	@SecurityControl(limits = "hdg.ExchangeWhitelist:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeWhitelist:list");
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model) {
	}

	@RequestMapping(value = "view.htm")
	public String view(Model model, Long id) {
		ExchangeWhiteList entity = exchangeWhiteListService.find(id);
		if (entity == null) {
			return SEGMENT_RECORD_NOT_FOUND;
		} else {
			model.addAttribute("entity", entity);
		}
		return "/security/hdg/exchange_whitelist/view";
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(ExchangeWhiteList exchangeWhiteList) {
		return exchangeWhiteListService.create(exchangeWhiteList);
	}

	@RequestMapping(value = "edit.htm")
	public String edit(Model model, Long id) {
		ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.find(id);
		model.addAttribute("entity", exchangeWhiteList);
		return "/security/hdg/exchange_whitelist/edit";
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(ExchangeWhiteList exchangeWhiteList) {
		return exchangeWhiteListService.update(exchangeWhiteList);
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(ExchangeWhiteList search) {
		return PageResult.successResult(exchangeWhiteListService.findPage(search));
	}

	@RequestMapping("delete_whitelist_customer.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult deleteWhitelistCustomer(Long id) {
		return exchangeWhiteListService.delete(id);
	}
}
