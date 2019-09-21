package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccount;
import cn.com.yusong.yhdg.common.domain.basic.PlatformAccountInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/platform_account_in_out_money")
public class PlatformAccountInOutMoneyController extends SecurityController {

	@Autowired
	PlatformAccountInOutMoneyService platformAccountInOutMoneyService;


	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(PlatformAccountInOutMoney search) {
		return PageResult.successResult(platformAccountInOutMoneyService.findPage(search));
	}

	@ViewModel(ViewModel.INNER_PAGE)
	@RequestMapping("view_record.htm")
	public void viewRecord(Long id, Model model) {
		PlatformAccountInOutMoney platformAccountInOutMoney = platformAccountInOutMoneyService.find(id);
		model.addAttribute("entity", platformAccountInOutMoney);
	}

}
