package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryParameterMapper extends MasterMapper {
    BatteryParameter find(@Param("id") String id);
    int insert(BatteryParameter batteryParameter);
    int update(BatteryParameter batteryParameter);
}
