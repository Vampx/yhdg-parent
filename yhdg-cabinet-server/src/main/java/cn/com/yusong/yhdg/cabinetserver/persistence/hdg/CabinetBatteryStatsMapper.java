package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CabinetBatteryStatsMapper extends MasterMapper {

    public CabinetBatteryStats findBefore(@Param("cabinetId") String cabinetId);

    int insert(CabinetBatteryStats cabinetBatteryStats);
}
