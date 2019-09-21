package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccountInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/platform_account")
public class PlatformAccountController extends SecurityController {

	@Autowired
	PlatformAccountService platformAccountService;
	@Autowired
	PlatformAccountInOutMoneyService platformAccountInOutMoneyService;

	@SecurityControl(limits = "basic.PlatformAccount:list")
	@RequestMapping("index.htm")
	public void index(Model model) {
		model.addAttribute(MENU_CODE_NAME, "basic.PlatformAccount:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(PlatformAccount search) {
		return PageResult.successResult(platformAccountService.findPage(search));
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view.htm")
	public void view(int id, Model model) {
		PlatformAccount platformAccount = platformAccountService.find(id);
		model.addAttribute("entity", platformAccount);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view_basic.htm")
	public void viewBasic(Integer id, Model model) {
		PlatformAccount platformAccount = platformAccountService.find(id);
		model.addAttribute("entity", platformAccount);
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view_in_out_money.htm")
	public void viewInOutMoney(Integer id, Model model) {
		PlatformAccount platformAccount = platformAccountService.find(id);
		model.addAttribute("entity", platformAccount);
	}

	@RequestMapping("in_out_money_page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult inOutMoneyPage(PlatformAccountInOutMoney search) {
		return PageResult.successResult(platformAccountInOutMoneyService.findPage(search));
	}

}
