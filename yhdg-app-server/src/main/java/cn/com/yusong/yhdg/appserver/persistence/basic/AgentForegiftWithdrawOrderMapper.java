package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftWithdrawOrder;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentForegiftWithdrawOrderMapper extends MasterMapper {
    AgentForegiftWithdrawOrder find(String id);

    int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category,  @Param("status") Integer status);

}
