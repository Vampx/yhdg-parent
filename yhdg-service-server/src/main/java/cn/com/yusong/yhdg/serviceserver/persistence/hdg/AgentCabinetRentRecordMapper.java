package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentCabinetRentRecordMapper extends MasterMapper {
    public AgentCabinetRentRecord find(@Param("agentId") int agentId, @Param("cabinetId") String cabinetId);
    public int insert(AgentCabinetRentRecord stats);
}
