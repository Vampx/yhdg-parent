package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryReportMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    BatteryReport find(BatteryReport batteryReport);
    BatteryReport findLast(@Param("batteryId") String batteryId, @Param("suffix") String suffix);
    int findPageCount(BatteryReport batteryReport);
    List<BatteryReport> findPageResult(BatteryReport batteryReport);
    List<BatteryReport> findList(@Param("batteryId") String batteryId, @Param("createTime") Date createTime, @Param("suffix") String suffix);

}
