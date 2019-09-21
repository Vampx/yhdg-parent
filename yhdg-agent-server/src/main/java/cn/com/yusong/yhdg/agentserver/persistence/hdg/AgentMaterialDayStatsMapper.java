package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AgentMaterialDayStatsMapper extends MasterMapper {
    AgentMaterialDayStats find(@Param("id") int id);
    public int findPageCount(AgentMaterialDayStats search);
    public List<AgentMaterialDayStats> findPageResult(AgentMaterialDayStats search);
    int updateStatus(@Param("id") int id, @Param("payType") Integer payType, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
}
