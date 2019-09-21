package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.entity.LocationInfo;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderBatteryReportLogService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/baidu_map")
public class BaiduMapController extends SecurityController {
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryOrderBatteryReportLogService batteryOrderBatteryReportLogService;

    @RequestMapping(value = "location.htm")
    public void location(Model model, String address, Double lng, Double lat, Integer readonly) {
        model.addAttribute("address", address);
        model.addAttribute("lng", lng);
        model.addAttribute("lat", lat);
        model.addAttribute("readonly", readonly);
    }

    @RequestMapping(value = "new_location.htm")
    public void newLocation(Model model, String address, Double lng, Double lat, Integer readonly) {
        if (lng == null || lat == null) {
            lng = Constant.LNG;
            lat = Constant.LAT;
        }
        model.addAttribute("address", address);
        model.addAttribute("lng", lng);
        model.addAttribute("lat", lat);
        model.addAttribute("readonly", readonly);
    }

    @RequestMapping(value = "cabinet_location.htm")
    public void cabinetLocation(Model model, String address, Double lng, Double lat, Integer readonly) {
        if (lng == null || lat == null) {
            lng = Constant.LNG;
            lat = Constant.LAT;
        }
        model.addAttribute("address", address);
        model.addAttribute("lng", lng);
        model.addAttribute("lat", lat);
        model.addAttribute("readonly", readonly);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view_map.htm")
    public String viewMap() {
        return "/security/basic/baidu_map/view_map";
    }

    @RequestMapping(value = "monitor_map.htm")
    public void monitorMap() {
    }

    @ResponseBody
    @RequestMapping("map_data.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult mapData(String orderId,Date startDate,Date endDate) {
        BatteryOrder entity = batteryOrderService.find(orderId);
        if (StringUtils.isNotEmpty(orderId) && entity.getCreateTime() != null) {
            List<LocationInfo> list = batteryOrderBatteryReportLogService.findAllMap(orderId, entity.getCreateTime(),startDate,endDate);
            return DataResult.successResult(list);
        } else if (orderId.equals("emptyData")) {
            List<LocationInfo> list = batteryOrderBatteryReportLogService.findAllMap("emptyData", new Date(), new Date(), new Date());
            return DataResult.successResult(list);
        } else {
            return DataResult.failResult("");
        }
    }
}
