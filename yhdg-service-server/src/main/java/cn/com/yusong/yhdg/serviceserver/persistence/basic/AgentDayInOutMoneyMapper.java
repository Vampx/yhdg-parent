package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentDayInOutMoneyMapper extends MasterMapper {
    public AgentDayInOutMoney find(@Param("agentId") Integer agentId, @Param("statsDate") String statsDatey);

    public int insert(AgentDayInOutMoney stats);

    public int update(AgentDayInOutMoney stats);
}
