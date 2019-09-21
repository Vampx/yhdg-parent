package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShopTotalStatsMapper extends MasterMapper {

    ShopTotalStats find(@Param("shopId") String shopId,@Param("category") Integer category, @Param("agentId") Integer agentId);
    ShopTotalStats sumAll(@Param("agentId") Integer agentId);
    List<ShopTotalStats> findListByAgentId(@Param("agentId") Integer agentId,
                                           @Param("keyword") String keyword,
                                           @Param("category") Integer category,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);
    ShopTotalStats findCountByAgentId(@Param("agentId") Integer agentId, @Param("category") Integer category);

}
