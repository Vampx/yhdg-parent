package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryChargeRecordDetailService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryChargeRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

/**
 *  电池充电记录
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_charge_record")
public class BatteryChargeRecordController extends SecurityController {

    @Autowired
    BatteryChargeRecordService batteryChargeRecordService;
    @Autowired
    BatteryChargeRecordDetailService batteryChargeRecordDetailService;
    @Autowired
    AgentService agentService;

    //@SecurityControl(limits = OperCodeConst.CODE_2_4_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_04_03.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryChargeRecord search) {
        return PageResult.successResult(batteryChargeRecordService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        model.addAttribute("id", id);
        return "/security/hdg/battery_charge_record/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        BatteryChargeRecord entity = batteryChargeRecordService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            entity.setTypeName(BatteryChargeRecord.Type.getName(entity.getType()));
            entity.setAgentName(agentService.find(entity.getAgentId()).getAgentName());
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_charge_record/view_basic";
    }

    @RequestMapping(value = "detail_list.htm")
    public String detailList(Model model, long id) {
        model.addAttribute("id", id);
        return "/security/hdg/battery_charge_record/detail_list";
    }

    @RequestMapping("detail_list_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult detailListPage(BatteryChargeRecordDetail search) {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        search.setSuffix(suffix);
        return PageResult.successResult(batteryChargeRecordDetailService.findPage(search));
    }


    @RequestMapping(value = "select_by_battery.htm")
    public String selectByBattery(Model model, String batteryId) {
        model.addAttribute("batteryId", batteryId);
        return "/security/hdg/battery_charge_record/select_by_battery";
    }

    @RequestMapping(value = "select_by_battery_info.htm")
    public String selectByBatteryInfo(Model model, String batteryId) {
        model.addAttribute("batteryId", batteryId);
        return "/security/hdg/battery_charge_record/select_by_battery_info";
    }

    @RequestMapping(value = "select_battery_charge_power_by_record.htm")
    public String selectBatteryChargePowerByRecord(Model model, Long id) {
        BatteryChargeRecord entity = batteryChargeRecordService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_charge_record/select_battery_charge_power_by_record";
    }

}
