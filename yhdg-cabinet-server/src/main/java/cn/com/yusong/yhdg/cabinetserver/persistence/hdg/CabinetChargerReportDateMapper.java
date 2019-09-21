package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetChargerReportDateMapper extends HistoryMapper {
    int update(@Param("reportDate") String reportDate, @Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
    int create(@Param("reportDate") String reportDate, @Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum);
}