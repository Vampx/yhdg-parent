package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentSettlement;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentSettlementMapper extends MasterMapper {
    AgentSettlement find(int id);

    int findPageCount(AgentSettlement agentSettlement);

    List<AgentSettlement> findPageResult(AgentSettlement agentSettlement);

}
