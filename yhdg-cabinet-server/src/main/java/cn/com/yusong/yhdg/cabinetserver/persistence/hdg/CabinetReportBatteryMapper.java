package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBattery;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetReportBatteryMapper extends HistoryMapper {
    int createTable(@Param("tableName") String tableName);
    String findTable(@Param("tableName") String tableName);
    int insert(CabinetReportBattery cabinetReportBattery);
}
