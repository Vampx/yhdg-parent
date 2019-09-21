package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetChargerReport;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReport;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetChargerReportMapper extends HistoryMapper {
    int createTable(@Param("tableName") String tableName);
    String findTable(@Param("tableName") String tableName);
    int insert(CabinetChargerReport cabinetChargerReport);
}
