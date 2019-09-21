package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryUtilize;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BatteryUtilizeMapper extends MasterMapper {

    public BatteryUtilize find(@Param("batteryId") String batteryId);

    public int insert(BatteryUtilize order);

    public int update(@Param("id") Long id, @Param("takeTime") Date takeTime, @Param("utilize") String utilize);

    public int delete(@Param("id") Long id);
}
