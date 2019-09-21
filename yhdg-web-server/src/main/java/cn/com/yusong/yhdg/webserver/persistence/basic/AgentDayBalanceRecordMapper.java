package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/7/9.
 */
public interface AgentDayBalanceRecordMapper extends MasterMapper {
    AgentDayBalanceRecord find(long id);
    List<AgentDayBalanceRecord> findBayagentlist(@Param("agentId") int agentId, @Param("bizType") Integer bizType);
    int findPageCount(AgentDayBalanceRecord dayBalanceRecord);
    List<AgentDayBalanceRecord> findPageResult(AgentDayBalanceRecord dayBalanceRecord);
    int confirm(@Param("id") long id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus,
                @Param("confirmTime") Date confirmTime, @Param("confirmUser") String confirmUser);
    int updateInfo(@Param("id") long id, @Param("orderId") String orderId, @Param("status") Integer status);
}
