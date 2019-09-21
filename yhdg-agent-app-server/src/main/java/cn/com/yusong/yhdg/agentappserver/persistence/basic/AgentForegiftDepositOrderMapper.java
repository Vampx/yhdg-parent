package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentForegiftDepositOrderMapper extends MasterMapper {
    AgentForegiftDepositOrder find(String id);

    List<AgentForegiftDepositOrder> findList( @Param("agentId") int agentId, @Param("category") Integer category, @Param("offset") int offset, @Param("limit") int limit);

    int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category, @Param("status") Integer status);

    int insert(AgentForegiftDepositOrder entity);

}