package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.ExchangeInstallmentDetailService;
import cn.com.yusong.yhdg.agentserver.service.hdg.ExchangeInstallmentSettingService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_detail")
public class ExchangeInstallmentDetailController extends SecurityController {
	@Autowired
	ExchangeInstallmentDetailService exchangeInstallmentDetailService;
	@Autowired
	ExchangeInstallmentSettingService exchangeInstallmentSettingService;

	@SecurityControl(limits = "hdg.ExchangeInstallmentDetail:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model, Long settingId) {
		model.addAttribute("settingId", settingId);
		model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentDetail:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(ExchangeInstallmentDetail search) {
		return PageResult.successResult(exchangeInstallmentDetailService.findPage(search));
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model, Long settingId) {
		model.addAttribute("settingId", settingId);
		List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = exchangeInstallmentDetailService.findListBySettingId(settingId);
		model.addAttribute("num", exchangeInstallmentDetailList.size() + 1);
		//总金额-首付金额
		ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingService.find(settingId);
		int recentMoney = 0;
		for (ExchangeInstallmentDetail exchangeInstallmentDetail : exchangeInstallmentDetailList) {
			recentMoney += exchangeInstallmentDetail.getMoney();
		}
		//分期剩余金额
		model.addAttribute("restMoney", exchangeInstallmentSetting.getTotalMoney() - recentMoney);
		//分期设置截至时间
		model.addAttribute("deadlineTime", exchangeInstallmentSetting.getDeadlineTime());
		//上一期截至时间
		if (exchangeInstallmentDetailList.size() > 0) {
			model.addAttribute("lastExpireTime", exchangeInstallmentDetailList.get(exchangeInstallmentDetailList.size() - 1).getExpireTime());
		}
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(ExchangeInstallmentDetail entity) {
		return exchangeInstallmentDetailService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public void edit(Model model, Long settingId, Integer num) {
		model.addAttribute("settingId", settingId);
		List<ExchangeInstallmentDetail> exchangeInstallmentDetailList = exchangeInstallmentDetailService.findListBySettingId(settingId);
		model.addAttribute("num", num);
		//总金额-首付金额
		ExchangeInstallmentSetting exchangeInstallmentSetting = exchangeInstallmentSettingService.find(settingId);
		int recentMoney = 0;
		int money = 0;
		for (ExchangeInstallmentDetail exchangeInstallmentDetail : exchangeInstallmentDetailList) {
			//存在上一期截至时间
			if (exchangeInstallmentDetail.getNum().intValue() == num - 1) {
				model.addAttribute("lastExpireTime", exchangeInstallmentDetail.getExpireTime());
			}
			if (exchangeInstallmentDetail.getNum().intValue() == num) {
				money = exchangeInstallmentDetail.getMoney();
				model.addAttribute("money", money);
				model.addAttribute("expireTime", exchangeInstallmentDetail.getExpireTime());
			}
			//存在下一期截至时间
			if (exchangeInstallmentDetail.getNum().intValue() == num + 1) {
				model.addAttribute("nextExpireTime", exchangeInstallmentDetail.getExpireTime());
				model.addAttribute("nextNum", num + 1);
			}
			recentMoney += exchangeInstallmentDetail.getMoney();
		}
		//分期剩余金额
		model.addAttribute("restMoney", exchangeInstallmentSetting.getTotalMoney() - recentMoney + money);
		//分期设置截至时间
		model.addAttribute("deadlineTime", exchangeInstallmentSetting.getDeadlineTime());
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(ExchangeInstallmentDetail entity) {
		return exchangeInstallmentDetailService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long settingId, Integer num) {
		return exchangeInstallmentDetailService.delete(settingId, num);
	}
}
