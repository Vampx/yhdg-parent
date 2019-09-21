package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.SwitchVehicleRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface SwitchVehicleRecordMapper extends MasterMapper {
    int findPageCount(SwitchVehicleRecord search);
    List<SwitchVehicleRecord> findPageResult(SwitchVehicleRecord search);
}
