package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBatteryDate;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CabinetReportBatteryDateMapper extends HistoryMapper {
    List<CabinetReportBatteryDate> findList(@Param("batteryId") String batteryId);
    CabinetReportBatteryDate findLast(@Param("batteryId") String batteryId);
    List<String> findYeah(@Param("batteryId") String batteryId);
    List<Map<String, String>> findMonth(@Param("batteryId") String batteryId);
}
