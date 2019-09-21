package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReportDate;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetChargerReportDate;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CabinetChargerReportDateMapper extends HistoryMapper {
    List<CabinetChargerReportDate> findList(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    CabinetChargerReportDate findLast(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    List<String> findYear(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    List<Map<String, String>> findMonth(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
}
