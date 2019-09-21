package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCompanyInOutMoneyMapper extends MasterMapper {
    AgentCompanyInOutMoney find(Long id);
    int findPageCount(AgentCompanyInOutMoney search);
    List<AgentCompanyInOutMoney> findPageResult(AgentCompanyInOutMoney search);
    int insert(AgentCompanyInOutMoney entity);

}
