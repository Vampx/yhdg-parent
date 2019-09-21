package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryParameterLogMapper extends MasterMapper {
    List<BatteryParameterLog> findByBatteryId(@Param("batteryId") String batteryId, @Param("status") Integer status);

    int report(@Param("id") Integer id, @Param("status") Integer status, @Param("reportTime") Date reportTime);

}
