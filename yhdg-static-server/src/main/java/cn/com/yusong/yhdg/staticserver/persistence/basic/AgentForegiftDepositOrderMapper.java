package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


public interface AgentForegiftDepositOrderMapper extends MasterMapper {
    AgentForegiftDepositOrder find(String id);

    int sumMoney(@Param("agentId") Integer agentId, @Param("category") Integer category, @Param("status") Integer status);

    public int payOk(@Param("id") String id, @Param("handleTime") Date handleTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);


}