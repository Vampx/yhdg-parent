package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CabinetTotalStatsMapper extends MasterMapper {
    CabinetTotalStats find(@Param("cabinetId") String cabinetId, @Param("agentId") Integer agentId);
    CabinetTotalStats sumAll( @Param("agentId") Integer agentId);
    List<CabinetTotalStats> findListByAgentId(@Param("agentId") Integer agentId,
                                              @Param("keyword") String keyword,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);
    CabinetTotalStats findCountByAgentId(@Param("agentId") Integer agentId);

}
