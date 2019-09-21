package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryChargeRecordMapper extends MasterMapper {

    BatteryChargeRecord find(@Param("id") long id);

    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findPageCount(BatteryChargeRecord batteryChargeRecord);

    List findPageResult(BatteryChargeRecord batteryChargeRecord);

}
