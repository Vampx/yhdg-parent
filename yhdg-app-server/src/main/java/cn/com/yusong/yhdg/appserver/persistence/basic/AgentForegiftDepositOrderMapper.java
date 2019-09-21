package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface AgentForegiftDepositOrderMapper extends MasterMapper {
    public AgentForegiftDepositOrder find(String id);

    public int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category,  @Param("status") Integer status);

}