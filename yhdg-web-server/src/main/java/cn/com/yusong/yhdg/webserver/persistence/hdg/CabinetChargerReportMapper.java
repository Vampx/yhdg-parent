package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryReport;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetChargerReport;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CabinetChargerReportMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    CabinetChargerReport find(CabinetChargerReport cabinetChargerReport);
    int findPageCount(CabinetChargerReport cabinetChargerReport);
    List<CabinetChargerReport> findPageResult(CabinetChargerReport cabinetChargerReport);

}
