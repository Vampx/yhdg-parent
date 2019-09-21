package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCell;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryCellRegular;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryCellRegularService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 电池电芯条码规则
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_cell_regular")
public class BatteryCellRegularController extends SecurityController {
	@Autowired
	BatteryCellRegularService batteryCellRegularService;

	@RequestMapping("create.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult create(BatteryCellRegular entity) {
		return batteryCellRegularService.create(entity);
	}

	@RequestMapping("update.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult update(BatteryCellRegular entity) {
		return batteryCellRegularService.update(entity);
	}

	@RequestMapping("check_param.htm")
	@ViewModel(ViewModel.JSON)
	@ResponseBody
	public ExtResult cehckParam(BatteryCellRegular entity) {
		return batteryCellRegularService.checkParam(entity);
	}
}
