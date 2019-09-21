package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface VehicleOnlineStatsMapper extends MasterMapper {
    int findPageCount(VehicleOnlineStats vehicleOnlineStats);

    List<VehicleOnlineStats> findPageResult(VehicleOnlineStats vehicleOnlineStats);
}
