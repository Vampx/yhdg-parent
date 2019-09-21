package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    BatteryReportLog find(BatteryReportLog batteryReportLog);
    int findPageCount(BatteryReportLog batteryReportLog);
    List<BatteryReportLog> findPageResult(BatteryReportLog batteryReportLog);
    List<BatteryReportLog> findList(@Param("batteryId") String batteryId, @Param("reportTime") Date reportTime, @Param("suffix") String suffix);

}
