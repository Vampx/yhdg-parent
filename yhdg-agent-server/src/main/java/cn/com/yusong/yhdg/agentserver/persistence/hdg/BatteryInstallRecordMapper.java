package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryInstallRecordMapper extends MasterMapper {
    BatteryInstallRecord find(@Param("id") int id);

    BatteryInstallRecord findByBatteryType(@Param("batteryType") Integer batteryType, @Param("agentId") Integer agentId);

    int findPageCount(BatteryInstallRecord entity);

    List<BatteryInstallRecord> findPageResult(BatteryInstallRecord entity);

    int insert(BatteryInstallRecord batteryInstallRecord);

    int update(@Param("id") int id, @Param("status") int status);
}
