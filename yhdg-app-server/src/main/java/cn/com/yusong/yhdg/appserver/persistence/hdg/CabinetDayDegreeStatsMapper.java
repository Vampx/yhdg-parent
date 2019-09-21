package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayDegreeStatsMapper extends MasterMapper {

    CabinetDayDegreeStats findLast(@Param("cabinetId") String cabinetId);

}
