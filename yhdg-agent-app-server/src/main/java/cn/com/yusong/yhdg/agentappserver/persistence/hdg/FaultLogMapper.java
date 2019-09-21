package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FaultLogMapper extends MasterMapper {
    List<FaultLog> findByBatteryId(@Param("agentId") int agentId, @Param("batteryId") String batteryId, @Param("offset") int offset,@Param("limit") int limit);
    int findCountByAgent(@Param("agentId") int agentId, @Param("faultType") int faultType, @Param("status") int status);
    List<FaultLog> findByAgent(@Param("agentId") int agentId, @Param("faultType") int faultType, @Param("status") int status, @Param("offset") int offset,@Param("limit") int limit);
    List<FaultLog> findByCabinetId(@Param("agentId") int agentId, @Param("cabinetId") String cabinetId, @Param("offset") int offset,@Param("limit") int limit);
}
