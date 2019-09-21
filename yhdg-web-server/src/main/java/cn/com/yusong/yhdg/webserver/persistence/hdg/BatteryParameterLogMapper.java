package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryParameterLogMapper extends MasterMapper {
    BatteryParameterLog find(Integer id);
    int findPageCount(BatteryParameterLog search);
    List<BatteryParameterLog> findPageResult(BatteryParameterLog search);
    void insert(BatteryParameterLog log);
    int cancel(@Param("batteryId") String batteryId, @Param("paramId") String paramId, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);
    int delete(Integer id);
}
