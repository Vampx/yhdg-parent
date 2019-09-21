package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayStatsMapper extends MasterMapper {

    CabinetDayStats findForCabinet(@Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate);
}
