package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentForegiftDepositOrderMapper extends MasterMapper {
    AgentForegiftDepositOrder find(String id);

    int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category, @Param("status") Integer status);

    int findPageCount(AgentForegiftDepositOrder search);

    List<AgentForegiftDepositOrder> findPageResult(AgentForegiftDepositOrder search);
}