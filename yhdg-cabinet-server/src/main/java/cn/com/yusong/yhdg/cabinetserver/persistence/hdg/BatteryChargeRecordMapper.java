package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BatteryChargeRecordMapper extends MasterMapper {
    BatteryChargeRecord find(@Param("id") Long id);

    int updateEnd(@Param("id") Long id, @Param("currentVolume") Integer currentVolume, @Param("endTime") Date endTime);

    int updateCurrentVolume(@Param("id") Long id, @Param("currentVolume") int currentVolume, @Param("reportTime") Date reportTime);

    int insert(BatteryChargeRecord batteryChargeRecord);
}
