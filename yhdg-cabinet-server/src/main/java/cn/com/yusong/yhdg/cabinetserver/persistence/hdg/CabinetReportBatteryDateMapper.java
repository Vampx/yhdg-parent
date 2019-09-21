package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetReportBatteryDateMapper extends HistoryMapper {
    int update(@Param("reportDate") String reportDate, @Param("batteryId") String batteryId);
    int create(@Param("reportDate") String reportDate, @Param("batteryId") String batteryId);

}