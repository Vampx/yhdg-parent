package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentMaterialDayStatsMapper extends MasterMapper {
    AgentMaterialDayStats find(@Param("agentId") int agentId, @Param("statsDate") String statsDate, @Param("category") Integer category);
    int findCountByStatus(@Param("agentId") int agentId, @Param("status") Integer status);
    AgentMaterialDayStats findTotalMoney(@Param("agentId") int agentId, @Param("category") Integer category);
    AgentMaterialDayStats findTotalRentMoney(@Param("agentId") int agentId,
                                             @Param("beginDate") String beginDate,
                                             @Param("endDate") String endDate,
                                             @Param("category") Integer category);


}
