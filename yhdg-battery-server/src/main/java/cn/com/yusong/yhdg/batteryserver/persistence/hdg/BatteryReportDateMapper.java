package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryReportDateMapper extends HistoryMapper {
    int update(@Param("reportDate") String reportDate, @Param("batteryId") String batteryId);
    int create(@Param("reportDate") String reportDate, @Param("batteryId") String batteryId);

}