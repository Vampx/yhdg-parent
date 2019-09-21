package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOnlineStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CabinetOnlineStatsMapper extends MasterMapper {

    public CabinetOnlineStats findMaxRecord(String cabinetId);
    public int insert(CabinetOnlineStats stats);
    public int updateEndTime(@Param("cabinetId") String cabinetId, @Param("endTime") Date endTime);
}
