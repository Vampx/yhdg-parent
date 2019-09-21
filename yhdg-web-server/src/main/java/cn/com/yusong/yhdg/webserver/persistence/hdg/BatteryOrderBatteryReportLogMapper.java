package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderBatteryReportLog;
import cn.com.yusong.yhdg.common.entity.LocationInfo;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 换电电池上报记录
 */
public interface BatteryOrderBatteryReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);

    List<LocationInfo> findAllMap(@Param("orderId") String orderId, @Param("suffix") String suffix, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Date findLastReportTime(@Param("orderId") String orderId, @Param("suffix") String suffix);

    List<BatteryOrderBatteryReportLog> findSelectPageResult(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog);

    int findAllMapCount(@Param("orderId") String orderId, @Param("suffix") String suffix, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int findPageCount(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog);

    List<BatteryOrderBatteryReportLog> findPageResult(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog);

    int updateAddress(BatteryOrderBatteryReportLog batteryOrderBatteryReportLog);
}
