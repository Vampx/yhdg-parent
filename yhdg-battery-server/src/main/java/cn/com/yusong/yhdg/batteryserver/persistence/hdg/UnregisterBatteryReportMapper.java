package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface UnregisterBatteryReportMapper extends HistoryMapper {
    UnregisterBatteryReport find(UnregisterBatteryReport unregisterBatteryReport);

    String findTable(String tableName);

    int createTable(@Param("tableName") String tableName);

    int insert(UnregisterBatteryReport unregisterBatteryReport);


}
