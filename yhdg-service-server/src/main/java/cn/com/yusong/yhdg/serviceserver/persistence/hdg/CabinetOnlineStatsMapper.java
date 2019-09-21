package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CabinetOnlineStatsMapper extends MasterMapper {

    CabinetOnlineStats findMaxRecord(String cabinetId);
    int updateEndTime(@Param("cabinetId") String cabinetId, @Param("endTime") Date endTime);
}
