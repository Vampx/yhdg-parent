package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.webserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOperateLogService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/shop_battery")
public class ShopBatteryController extends SecurityController {
	static Logger log = LoggerFactory.getLogger(BatteryController.class);

	@Autowired
	BatteryService batteryService;
	@Autowired
	AgentService agentService;
	@Autowired
	DictItemService dictItemService;
	@Autowired
	BatteryOperateLogService batteryOperateLogService;
	@Autowired
	CustomerService customerService;
	@Autowired
    CabinetBoxService cabinetBoxService;
	@Autowired
	CabinetService cabinetService;

//	@SecurityControl(limits = OperCodeConst.CODE_4_1_3_1)
	@RequestMapping(value = "index.htm")
	public void index(Model model) {
//		model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_03.getValue());
		model.addAttribute("StatusEnum", Battery.Status.values());
		model.addAttribute("repairStatusList", Battery.RepairStatus.values());
	//	model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
		model.addAttribute("batteryChargeStatusList", Battery.ChargeStatus.values());
		model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
	}

	/*@ResponseBody
	@RequestMapping("find_battery_count.htm")
	@ViewModel(ViewModel.JSON)
	public ExtResult findBatteryCount(String cabinetId) {
//		站点电池=(柜子里的电池 + B端骑手的电池)
//		当前缺电池数=（站点的battery_count - (柜子里的电池 + B端骑手的电池) ）
//		骑手电池=站点的B端骑手中的电池数量
//		柜子电池=站点中的电池数
		//B端骑手的电池
		Integer batteryCount = customerService.findBatteryCountByType(cabinetId);
		//柜子里的电池
		Integer boxCount = cabinetBoxService.findBatteryCountByStatus(cabinetId, Battery.Status.IN_BOX.getValue());
		//站点电池
		Integer cabinetBatteryCount = batteryCount + boxCount;
		//当前缺电池数
		Cabinet cabinet = cabinetService.find(cabinetId);
		Integer lessBatteryCount = cabinet.getBatteryCount() - cabinetBatteryCount;
		Battery battery = new Battery();
		battery.setBatteryCount(batteryCount);
		battery.setBoxCount(boxCount);
		battery.setCabinetBatteryCount(cabinetBatteryCount);
		battery.setLessBatteryCount(lessBatteryCount);
		return DataResult.successResult(battery);
	}*/

}