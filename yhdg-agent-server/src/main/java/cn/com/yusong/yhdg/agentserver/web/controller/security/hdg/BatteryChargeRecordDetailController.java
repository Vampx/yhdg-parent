package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryChargeRecordDetailService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;

/**
 * 电池充电记录详情
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_charge_record_detail")
public class BatteryChargeRecordDetailController extends SecurityController {
    @Autowired
    BatteryChargeRecordDetailService batteryChargeRecordDetailService;

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryChargeRecordDetail search) {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        search.setSuffix(suffix);
        return PageResult.successResult(batteryChargeRecordDetailService.findPage(search));
    }

    @RequestMapping(value = "view_line_chart.htm")
    public String viewLineChar(Model model, Long id) {
        model.addAttribute("id", id);
        List<BatteryChargeRecordDetail> list = batteryChargeRecordDetailService.find(id);
        model.addAttribute("list", list);
        return "/security/hdg/battery_charge_record_detail/view_line_chart";
    }
}
