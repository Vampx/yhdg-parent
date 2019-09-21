package cn.com.yusong.yhdg.vehicleserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface VehicleOnlineStatsMapper extends MasterMapper {

    public VehicleOnlineStats findMaxRecord(int vehicleId);
    public int insert(VehicleOnlineStats stats);
    public int updateEndTime(@Param("vehicleId") Integer vehicleId, @Param("endTime") Date endTime);
}
