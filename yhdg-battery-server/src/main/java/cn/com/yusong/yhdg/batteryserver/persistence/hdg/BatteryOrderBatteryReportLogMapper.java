package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryOrderBatteryReportLogMapper extends HistoryMapper {
    int createTable(@Param("tableName") String tableName);
    int insert(BatteryOrderBatteryReportLog batteryReportLog);
}
