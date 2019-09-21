package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnregisterBatteryReportLogMapper extends HistoryMapper {
    UnregisterBatteryReportLog find(UnregisterBatteryReportLog unregisterBatteryReportLog);

    String findTable(String tableName);

    int createTable(@Param("tableName") String tableName);

    int insert(UnregisterBatteryReportLog unregisterBatteryReportLog);

    int update(UnregisterBatteryReportLog unregisterBatteryReportLog);

}
