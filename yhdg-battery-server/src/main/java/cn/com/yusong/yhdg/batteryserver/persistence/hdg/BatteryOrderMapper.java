package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryOrderMapper extends MasterMapper {
    public int update(@Param("id") String id, @Param("currentVolume") Integer currentVolume, @Param("currentDistance") Integer currentDistance, @Param("currentCapacity") Integer currentCapacity);

    int updateOrderStatus(@Param("id") String id, @Param("fromOrderStatus") Integer fromStatus, @Param("toOrderStatus") Integer toStatus);

    public BatteryOrder find(@Param("id") String id);
}
