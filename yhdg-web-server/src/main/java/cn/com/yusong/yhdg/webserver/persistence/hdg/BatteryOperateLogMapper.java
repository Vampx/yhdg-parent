package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电池操作日志
 */
public interface BatteryOperateLogMapper extends MasterMapper {
    List<BatteryOperateLog> findList(@Param("id") Long id, @Param("batteryId") String batteryId);
    int delete(String batteryId);
}
