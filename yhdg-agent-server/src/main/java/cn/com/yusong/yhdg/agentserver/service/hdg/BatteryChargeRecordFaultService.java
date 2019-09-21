package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecordFault;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryChargeRecordFaultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryChargeRecordFaultService {
    @Autowired
    BatteryChargeRecordFaultMapper batteryChargeRecordFaultMapper;

    public Page findPage(BatteryChargeRecordFault search) {
        Page<BatteryChargeRecordFault> page = new Page<BatteryChargeRecordFault>();
        List<BatteryChargeRecordFault> list = batteryChargeRecordFaultMapper.findPageResult(search);
        page.setTotalItems(list.size());
        page.setResult(list);
        return  page;
    }
}
