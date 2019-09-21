package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VehicleReportDate;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VehicleReportDateMapper extends HistoryMapper {
    List<VehicleReportDate> findList(@Param("vehicleId") String vehicleId);
    List<String> findYeah(@Param("vehicleId") String vehicleId);
    List<Map<String, String>> findMonth(@Param("vehicleId") String vehicleId);
}
