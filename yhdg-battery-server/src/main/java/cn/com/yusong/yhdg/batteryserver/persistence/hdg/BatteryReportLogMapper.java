package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryReportLogMapper extends HistoryMapper {
    int createTable(@Param("tableName") String tableName);
    String findTable(@Param("tableName") String tableName);
    int insert(BatteryReportLog batteryReportLog);
}
