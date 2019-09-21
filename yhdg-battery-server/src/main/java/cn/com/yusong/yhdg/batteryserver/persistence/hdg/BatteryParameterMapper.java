package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryParameterMapper extends MasterMapper {
    BatteryParameter find(@Param("id") String id);
    int insert(BatteryParameter batteryParameter);
    int update(BatteryParameter batteryParameter);
}
