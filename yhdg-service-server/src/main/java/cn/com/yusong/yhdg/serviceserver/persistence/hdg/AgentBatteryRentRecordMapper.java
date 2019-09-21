package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentBatteryRentRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentBatteryRentRecordMapper extends MasterMapper {
    public AgentBatteryRentRecord find(@Param("agentId") int agentId, @Param("batteryId") String batteryId);
    public int insert(AgentBatteryRentRecord stats);
}
