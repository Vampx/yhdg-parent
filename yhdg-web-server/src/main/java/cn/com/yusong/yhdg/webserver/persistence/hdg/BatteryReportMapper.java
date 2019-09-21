package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryReportMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    BatteryReport findByTime(@Param("batteryId") String batteryId, @Param("createTime") Date createTime, @Param("suffix") String suffix);
    BatteryReport findById(@Param("id") Integer id, @Param("suffix") String suffix);
    BatteryReport findLast(@Param("batteryId") String batteryId, @Param("suffix") String suffix);
    int findPageCount(BatteryReport batteryReport);
    int findBeforePageCount(BatteryReport batteryReport);
    int findAfterPageCount(BatteryReport batteryReport);
    List<BatteryReport> findPageResult(BatteryReport batteryReport);
    List<BatteryReport> findBeforePageResult(BatteryReport batteryReport);
    List<BatteryReport> findAfterPageResult(BatteryReport batteryReport);
    List<BatteryReport> findList(@Param("batteryId") String batteryId, @Param("createTime") Date createTime, @Param("suffix") String suffix);

}
