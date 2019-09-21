package cn.com.yusong.yhdg.routeserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatterySimReplaceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface BatterySimReplaceRecordMapper extends MasterMapper {
    public int insert(BatterySimReplaceRecord record);
}
