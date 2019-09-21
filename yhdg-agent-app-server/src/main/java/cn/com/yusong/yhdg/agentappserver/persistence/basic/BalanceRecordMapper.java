package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BalanceRecordMapper extends MasterMapper {
    BalanceRecord findByBalanceDate(@Param("agentId") int agentId,
                                    @Param("balanceDate") String balanceDate,
                                    @Param("category") Integer category);
    BalanceRecord findTotalMoney(@Param("agentId") int agentId,
                                 @Param("bizType") int bizType,
                                 @Param("category") Integer category);
    List<BalanceRecord> findDateRange(@Param("agentId") int agentId,
                                      @Param("bizType") int bizType,
                                      @Param("beginDate") String beginDate,
                                      @Param("endDate") String endDate,
                                      @Param("category") Integer category);
    BalanceRecord findTotalDateRange(@Param("agentId") int agentId,
                                     @Param("bizType") int bizType,
                                     @Param("beginDate") String beginDate,
                                     @Param("endDate") String endDate,
                                     @Param("category") Integer category);
}
