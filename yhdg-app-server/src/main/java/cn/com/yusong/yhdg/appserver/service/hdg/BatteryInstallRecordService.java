package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryInstallRecordMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryTypeIncomeRatioMapper;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BatteryInstallRecordService {
    @Autowired
    BatteryInstallRecordMapper batteryInstallRecordMapper;
    @Autowired
    BatteryTypeIncomeRatioMapper batteryTypeIncomeRatioMapper;
    @Autowired
    BatteryMapper batteryMapper;

    public int insert(BatteryInstallRecord entity){
        BatteryTypeIncomeRatio batteryTypeIncomeRatio = batteryTypeIncomeRatioMapper.find(entity.getAgentId());
        if (batteryTypeIncomeRatio.getIsReview() == CabinetIncomeTemplate.IsReview.YES.getValue()) {
            entity.setStatus(CabinetInstallRecord.Status.APPROVE.getValue());
            batteryMapper.updateUpLineStatus(entity.getBatteryId(), Cabinet.UpLineStatus.ONLINE.getValue());
        }else {
            batteryMapper.updateUpLineStatus(entity.getBatteryId(), Cabinet.UpLineStatus.APPLY_FOR_ONLINE.getValue());
            entity.setStatus(CabinetInstallRecord.Status.UNREVIEWED.getValue());
        }
        entity.setCreateTime(new Date());
        return batteryInstallRecordMapper.insert(entity);
    }
}
