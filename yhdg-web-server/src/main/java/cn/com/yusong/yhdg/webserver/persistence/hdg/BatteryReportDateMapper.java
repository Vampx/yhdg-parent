package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportDate;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BatteryReportDateMapper extends HistoryMapper {
    List<BatteryReportDate> findList(@Param("batteryId") String batteryId);
    BatteryReportDate findLast(@Param("batteryId") String batteryId);
    List<String> findYeah(@Param("batteryId") String batteryId);
    List<Map<String, String>> findMonth(@Param("batteryId") String batteryId);
}
