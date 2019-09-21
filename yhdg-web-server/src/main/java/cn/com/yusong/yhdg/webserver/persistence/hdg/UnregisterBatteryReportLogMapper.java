package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnregisterBatteryReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);

    UnregisterBatteryReportLog find(UnregisterBatteryReportLog unregisterBatteryReportLog);

    int findPageCount(UnregisterBatteryReportLog unregisterBatteryReportLog);

    List<UnregisterBatteryReportLog> findPageResult(UnregisterBatteryReportLog unregisterBatteryReportLog);
}
