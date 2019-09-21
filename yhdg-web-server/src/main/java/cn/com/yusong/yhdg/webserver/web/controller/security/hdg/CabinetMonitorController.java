package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.entity.OrderDateCount;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.BatteryOrderBoxStats;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.KeepPutOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_monitor")
public class CabinetMonitorController extends SecurityController {


    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    KeepPutOrderService keepPutOrderService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_8_1_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_08_01.getValue());
    }

    @RequestMapping(value = "load_data.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult loadData(Integer agentId, String cabinetId, Date startDate, Date endDate) {
        int dayCount = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 3600 / 24)+1;
        if (dayCount == 0) {
            dayCount = 1;
        }

        Date prevEndDate = startDate;
        Date prevStartDate = prevEndDate;
        if (dayCount != 1) {
            prevStartDate = DateUtils.addDays(prevEndDate, -dayCount);
        }

        String startDateOrderIdPrefix = OrderId.PREFIX_BATTERY + DateFormatUtils.format(startDate, OrderId.DATE_FORMAT);
        String endDateOrderIdPrefix = OrderId.PREFIX_BATTERY + DateFormatUtils.format(DateUtils.addDays(endDate, +1), OrderId.DATE_FORMAT);

        String prevStartDateOrderIdPrefix = OrderId.PREFIX_BATTERY + DateFormatUtils.format(prevStartDate, OrderId.DATE_FORMAT);
        String prevEndDateOrderIdPrefix = OrderId.PREFIX_BATTERY + DateFormatUtils.format(prevEndDate, OrderId.DATE_FORMAT);

        BatteryOrderBoxStats stats = new BatteryOrderBoxStats();
        stats.boxCount = cabinetBoxService.statsBoxCount(agentId, cabinetId);
        stats.orderCount = batteryOrderService.findOrderCount(agentId, startDateOrderIdPrefix, endDateOrderIdPrefix, cabinetId);
        stats.putCount = keepPutOrderService.findOrderCount(agentId, startDate, endDate, cabinetId);
        int prevOrderCount = batteryOrderService.findOrderCount(agentId, prevStartDateOrderIdPrefix, prevEndDateOrderIdPrefix, cabinetId);
        if (prevOrderCount == 0) {
            if (stats.orderCount == 0) {
                stats.growthRate = "0";
            } else {
                stats.growthRate = "100";
            }
        } else {
            stats.growthRate = String.format("%.2f", (double) (stats.orderCount - prevOrderCount) / prevOrderCount * 100);
        }

        List<String> dateList = new ArrayList<String>();
        List<String> prevDateList = new ArrayList<String>();

        List<Integer> dateOrderCount = new ArrayList<Integer>();
        List<Integer> prevDateOrderCount = new ArrayList<Integer>();

        Map<String, Integer> dateCountMap = new HashMap<String, Integer>();
        Map<String, Integer> prevDateCountMap = new HashMap<String, Integer>();

        List<Double> growthRateList = new ArrayList<Double>();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        while (calendar.getTimeInMillis() <= endDate.getTime()) {
            String date = DateFormatUtils.format(calendar, Constant.DATE_FORMAT);
            dateList.add(date);
            dateCountMap.put(date, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<OrderDateCount> orderDateCountList = batteryOrderService.findDateOrderCount(agentId, startDateOrderIdPrefix, endDateOrderIdPrefix, cabinetId);
        for (OrderDateCount e : orderDateCountList) {
            dateCountMap.put(e.getDate(), e.getNum());
        }
        for (String date : dateList) {
            dateOrderCount.add(dateCountMap.get(date));
        }

        calendar = new GregorianCalendar();
        calendar.setTime(prevStartDate);
        while (calendar.getTimeInMillis() <= prevEndDate.getTime()) {
            String date = DateFormatUtils.format(calendar, Constant.DATE_FORMAT);
            prevDateList.add(date);
            prevDateCountMap.put(date, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<OrderDateCount> prevOrderDateCountList = batteryOrderService.findDateOrderCount(agentId, prevStartDateOrderIdPrefix, prevEndDateOrderIdPrefix, cabinetId);
        for (OrderDateCount e : prevOrderDateCountList) {
            prevDateCountMap.put(e.getDate(), e.getNum());
        }
        for (String date : prevDateList) {
            prevDateOrderCount.add(prevDateCountMap.get(date));
        }

        for (int i = 0, len = dateList.size(); i < len; i++) {
            int count = dateCountMap.get(dateList.get(i));
            int prevCount = prevDateCountMap.get(prevDateList.get(i));

            if (prevCount == 0) {
                growthRateList.add(1d * count);
            } else {
                growthRateList.add(1d * count / prevCount);
            }
        }

        stats.dateList = dateList;
        stats.orderCountList = dateOrderCount;
        stats.growthRateList = growthRateList;

        return DataResult.successResult(null, stats);
    }

}
