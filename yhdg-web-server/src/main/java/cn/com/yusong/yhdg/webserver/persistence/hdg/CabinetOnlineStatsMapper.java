package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface CabinetOnlineStatsMapper extends MasterMapper {
    int findPageCount(CabinetOnlineStats cabinetOnlineStats);

    List findPageResult(CabinetOnlineStats cabinetOnlineStats);
}
