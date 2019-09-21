package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftWithdrawOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentForegiftWithdrawOrderMapper extends MasterMapper {
    AgentForegiftWithdrawOrder find(String id);

    int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category, @Param("status") Integer status);

}
