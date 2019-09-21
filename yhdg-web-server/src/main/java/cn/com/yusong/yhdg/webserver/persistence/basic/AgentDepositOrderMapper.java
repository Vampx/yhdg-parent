package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentDepositOrderMapper extends MasterMapper {
    AgentDepositOrder find(String id);

    int findPageCount(AgentDepositOrder search);

    List<AgentDepositOrder> findPageResult(AgentDepositOrder search);

    int insert(AgentDepositOrder entity);

    int update(AgentDepositOrder entity);

}