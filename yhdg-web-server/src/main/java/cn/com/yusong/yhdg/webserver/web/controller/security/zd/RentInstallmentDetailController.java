package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentDetailService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentSettingService;
import cn.com.yusong.yhdg.webserver.service.zd.RentInstallmentDetailService;
import cn.com.yusong.yhdg.webserver.service.zd.RentInstallmentSettingService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping(value = "/security/zd/rent_installment_detail")
public class RentInstallmentDetailController extends SecurityController {
	@Autowired
	RentInstallmentDetailService rentInstallmentDetailService;
	@Autowired
	RentInstallmentSettingService rentInstallmentSettingService;

	@SecurityControl(limits = "zd.RentInstallmentDetail:list")
	@RequestMapping(value = "index.htm")
	public void index(Model model, Long settingId) {
		model.addAttribute("settingId", settingId);
		model.addAttribute(MENU_CODE_NAME, "zd.RentInstallmentDetail:list");
	}

	@RequestMapping("page.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public PageResult page(RentInstallmentDetail search) {
		return PageResult.successResult(rentInstallmentDetailService.findPage(search));
	}

	@RequestMapping(value = "add.htm")
	public void add(Model model, Long settingId) {
		model.addAttribute("settingId", settingId);
		List<RentInstallmentDetail> rentInstallmentDetailList = rentInstallmentDetailService.findListBySettingId(settingId);
		model.addAttribute("num", rentInstallmentDetailList.size() + 1);
		//总金额-首付金额
		RentInstallmentSetting rentInstallmentSetting = rentInstallmentSettingService.find(settingId);
		int recentMoney = 0;
		for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
			recentMoney += rentInstallmentDetail.getMoney();
		}
		//分期剩余金额
		model.addAttribute("restMoney", rentInstallmentSetting.getTotalMoney() - recentMoney);
		//分期设置截至时间
		model.addAttribute("deadlineTime", rentInstallmentSetting.getDeadlineTime());
		//上一期截至时间
		if (rentInstallmentDetailList.size() > 0) {
			model.addAttribute("lastExpireTime", rentInstallmentDetailList.get(rentInstallmentDetailList.size() - 1).getExpireTime());
		}
	}

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(RentInstallmentDetail entity) {
		return rentInstallmentDetailService.create(entity);
	}

	@RequestMapping(value = "edit.htm")
	public void edit(Model model, Long settingId, Integer num) {
		model.addAttribute("settingId", settingId);
		List<RentInstallmentDetail> rentInstallmentDetailList = rentInstallmentDetailService.findListBySettingId(settingId);
		model.addAttribute("num", num);
		//总金额-首付金额
		RentInstallmentSetting rentInstallmentSetting = rentInstallmentSettingService.find(settingId);
		int recentMoney = 0;
		int money = 0;
		for (RentInstallmentDetail rentInstallmentDetail : rentInstallmentDetailList) {
			//存在上一期截至时间
			if (rentInstallmentDetail.getNum().intValue() == num - 1) {
				model.addAttribute("lastExpireTime", rentInstallmentDetail.getExpireTime());
			}
			if (rentInstallmentDetail.getNum().intValue() == num) {
				money = rentInstallmentDetail.getMoney();
				model.addAttribute("money", money);
				model.addAttribute("expireTime", rentInstallmentDetail.getExpireTime());
			}
			//存在下一期截至时间
			if (rentInstallmentDetail.getNum().intValue() == num + 1) {
				model.addAttribute("nextExpireTime", rentInstallmentDetail.getExpireTime());
				model.addAttribute("nextNum", num + 1);
			}
			recentMoney += rentInstallmentDetail.getMoney();
		}
		//分期剩余金额
		model.addAttribute("restMoney", rentInstallmentSetting.getTotalMoney() - recentMoney + money);
		//分期设置截至时间
		model.addAttribute("deadlineTime", rentInstallmentSetting.getDeadlineTime());
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(RentInstallmentDetail entity) {
		return rentInstallmentDetailService.update(entity);
	}

	@RequestMapping("delete.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult delete(Long settingId, Integer num) {
		return rentInstallmentDetailService.delete(settingId, num);
	}
}
