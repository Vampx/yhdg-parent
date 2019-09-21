package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface AgentDayBalanceRecordMapper extends MasterMapper {
    AgentDayBalanceRecord find(long id);
    AgentDayBalanceRecord findByBalanceDate(@Param("agentId") int agentId, @Param("balanceDate") String balanceDate);
    void insert(AgentDayBalanceRecord agentDayBalanceRecord);
}
