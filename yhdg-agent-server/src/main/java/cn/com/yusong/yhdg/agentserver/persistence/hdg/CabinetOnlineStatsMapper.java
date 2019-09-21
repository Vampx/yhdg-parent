package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface CabinetOnlineStatsMapper extends MasterMapper {
    int findPageCount(CabinetOnlineStats cabinetOnlineStats);

    List findPageResult(CabinetOnlineStats cabinetOnlineStats);
}
