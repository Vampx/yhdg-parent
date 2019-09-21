package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBattery;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;

import java.util.List;

public interface CabinetReportBatteryMapper extends HistoryMapper {
    CabinetReportBattery find(CabinetReportBattery cabinetReportBattery);
    int findPageCount(CabinetReportBattery cabinetReportBattery);
    List<CabinetReportBattery> findPageResult(CabinetReportBattery cabinetReportBattery);

}
