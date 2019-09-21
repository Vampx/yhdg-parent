package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryChargeRecordDetailMapper;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class BatteryChargeRecordDetailService {
    @Autowired
    BatteryChargeRecordDetailMapper batteryChargeRecordDetailMapper;

    public Page findPage(BatteryChargeRecordDetail search) {
        Page page = search.buildPage();
        List<String> tables = batteryChargeRecordDetailMapper.findTable("hdg_battery_charge_record_detail_" + search.getSuffix());
        if (tables.isEmpty()) {
            search.setBeginIndex(page.getOffset());
            return page;
        } else {
            page.setTotalItems(batteryChargeRecordDetailMapper.findPageCount(search));
            search.setBeginIndex(page.getOffset());
            page.setResult(batteryChargeRecordDetailMapper.findPageResult(search));
        }
        return page;
    }

    public List<BatteryChargeRecordDetail> find(Long id) {
        Calendar calendar = Calendar.getInstance();
        String suffix = DateFormatUtils.format(calendar.getTime(), "yyyyww");
        List<String> tables = batteryChargeRecordDetailMapper.findTable("hdg_battery_charge_record_detail_" + suffix);
        List<BatteryChargeRecordDetail> list = null;
        if (tables.isEmpty()) {
            list = null;
        } else {
            list = batteryChargeRecordDetailMapper.find(id, suffix);
        }
        return list;
    }
}
