package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportDate;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CabinetReportDateMapper extends HistoryMapper {
    List<CabinetReportDate> findList(@Param("cabinetId") String cabinetId);
    CabinetReportDate findLast(@Param("cabinetId") String cabinetId);
    List<String> findYeah(@Param("cabinetId") String cabinetId);
    List<Map<String, String>> findMonth(@Param("cabinetId") String cabinetId);
}
