package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentInOutMoneyMapper extends MasterMapper {
    AgentInOutMoney find(@Param("id") Long id);
    int findPageCount(AgentInOutMoney search);
    List<AgentInOutMoney> findPageResult(AgentInOutMoney search);
    int insert(AgentInOutMoney entity);
}
