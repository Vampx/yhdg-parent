package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VehicleReportLog;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleReportLogMapper extends HistoryMapper {
    String findTableExist(@Param("tableName") String tableName);
    VehicleReportLog find(VehicleReportLog vehicleReportLog);
    int findPageCount(VehicleReportLog vehicleReportLog);
    List<VehicleReportLog> findPageResult(VehicleReportLog vehicleReportLog);
}
