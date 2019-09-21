package cn.com.yusong.yhdg.routeserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatterySimReplaceRecord;
import cn.com.yusong.yhdg.routeserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.routeserver.persistence.hdg.BatterySimReplaceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatteryService {
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    BatterySimReplaceRecordMapper batterySimReplaceRecordMapper;

    public Battery find(String id) {
        return batteryMapper.find(id);
    }

    public int updateSimMemo(String id, String oldCode, String newCode) {
        int effect = batteryMapper.updateSimMemo(id, newCode);

        if(effect > 0) {
            BatterySimReplaceRecord replaceRecord = new BatterySimReplaceRecord();
            replaceRecord.setBatteryId(id);
            replaceRecord.setOldCode(oldCode);
            replaceRecord.setNewCode(newCode);
            replaceRecord.setOperator("system");
            replaceRecord.setCreateTime(new Date());
            batterySimReplaceRecordMapper.insert(replaceRecord);
        }

        return effect;
    }
}
