package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface BalanceRecordMapper extends MasterMapper {
    BalanceRecord find(long id);
    BalanceRecord findByBalanceDate(@Param("partnerId") Integer partnerId,  @Param("agentId") Integer agentId, @Param("shopId") String shopId,  @Param("bizType") int bizType, @Param("balanceDate") String balanceDate, @Param("category") Integer category);
    BalanceRecord findByAgentCompanyBalanceDate(@Param("partnerId") Integer partnerId,  @Param("agentId") Integer agentId, @Param("agentCompanyId") String agentCompanyId,  @Param("bizType") int bizType, @Param("balanceDate") String balanceDate, @Param("category") Integer category);
    void insert(BalanceRecord balanceRecord);
}
