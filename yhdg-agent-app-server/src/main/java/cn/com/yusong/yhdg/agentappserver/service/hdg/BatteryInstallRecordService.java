package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.BatteryInstallRecordMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatteryInstallRecordService {
    @Autowired
    BatteryInstallRecordMapper batteryInstallRecordMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public int insert(BatteryInstallRecord entity, Integer category){
        entity.setStatus(BatteryInstallRecord.Status.YESONLINE.getValue());
        batteryMapper.updateUpLineStatus(entity.getBatteryType(), entity.getAgentId(), entity.getBatteryId(), BatteryInstallRecord.Status.YESONLINE.getValue(), category, new Date());
        entity.setCreateTime(new Date());
        return batteryInstallRecordMapper.insert(entity);
    }

}
