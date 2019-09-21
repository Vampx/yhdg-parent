package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderBatteryReportLogService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * 换电电池上报记录
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_order_battery_report_log")
public class BatteryOrderBatteryReportLogController extends SecurityController {
    @Autowired
    BatteryOrderBatteryReportLogService batteryOrderBatteryReportLogService;
    @Autowired
    BatteryOrderService batteryOrderService;

    //@SecurityControl(limits = OperCodeConst.CODE_2_3_6_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_03_06.getValue());
    }

    @RequestMapping(value = "select_index.htm")
    public String selectIndex(Model model, String orderId, Date startDate, Date endDate) {
        model.addAttribute("orderId", orderId);
        model.addAttribute("startDate", startDate != null ? DateFormatUtils.format(startDate, Constant.DATE_TIME_FORMAT) : startDate);
        model.addAttribute("endDate", endDate != null ? DateFormatUtils.format(endDate, Constant.DATE_TIME_FORMAT) : endDate);
        return "/security/hdg/battery_order_battery_report_log/select_index";
    }

    @ResponseBody
    @RequestMapping("select_page.htm")
    @ViewModel(ViewModel.JSON)
    public PageResult selectPage(BatteryOrderBatteryReportLog search) {
        BatteryOrder entity = batteryOrderService.find(search.getOrderId());
        if (StringUtils.isNotEmpty(search.getOrderId()) &&entity!=null&& entity.getCreateTime() != null) {
            String suffix = DateFormatUtils.format(entity.getCreateTime(), "yyyyww");
            search.setSuffix(suffix);
            return PageResult.successResult(batteryOrderBatteryReportLogService.findselectPage(search));
        } else {
            return PageResult.emptyResult();
        }
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryOrderBatteryReportLog search) {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        search.setSuffix(suffix);

        return PageResult.successResult(batteryOrderBatteryReportLogService.findPage(search));
    }

    @ResponseBody
    @RequestMapping("find_all_map_count.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findAllMapCount(String orderId) {
        BatteryOrder entity = batteryOrderService.find(orderId);
        if (StringUtils.isNotEmpty(orderId) &&entity!=null&& entity.getCreateTime() != null) {
            return batteryOrderBatteryReportLogService.findAllMapCount(orderId, entity.getCreateTime());
        } else {
            return ExtResult.failResult("");
        }
    }

    @ResponseBody
    @RequestMapping("update_address.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateAddress(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog) {
        BatteryOrder entity = batteryOrderService.find(batteryOrderBatteryReportLog.getOrderId());
        if (StringUtils.isNotEmpty(batteryOrderBatteryReportLog.getOrderId()) && batteryOrderBatteryReportLog.getReportTime() != null &&entity!=null&& entity.getCreateTime() != null) {
            String suffix = DateFormatUtils.format(entity.getCreateTime(), "yyyyww");
            batteryOrderBatteryReportLog.setSuffix(suffix);
            return batteryOrderBatteryReportLogService.updateAddress(batteryOrderBatteryReportLog);
        } else {
            return ExtResult.failResult("");
        }
    }
}
