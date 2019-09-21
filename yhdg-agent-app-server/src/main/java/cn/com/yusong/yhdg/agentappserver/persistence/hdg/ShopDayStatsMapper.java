package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDayStatsMapper extends MasterMapper {
    ShopDayStats find(@Param("agentId") int agentId,
                      @Param("shopId") String shopId,
                      @Param("statsDate") String statsDate,
                      @Param("category") Integer category);

    List<ShopDayStats> findTotalShopStatsList(@Param("agentId") Integer agentId,
                                              @Param("category") Integer category,
                                              @Param("beginDate") String beginDate,
                                              @Param("endDate") String endDate, @Param("keyword") String keyword,
                                              @Param("offset") Integer offset, @Param("limit") Integer limit);
    ShopDayStats  findTotalStatsListShopId(@Param("agentId") Integer agentId,
                                           @Param("shopId") String shopId,
                                           @Param("category") Integer category,
                                           @Param("beginDate") String beginDate,
                                           @Param("endDate") String endDate);
    List<ShopDayStats> findDateRange(@Param("agentId") int agentId,
                                     @Param("shopId") String shopId,
                                     @Param("beginDate") String beginDate,
                                     @Param("endDate") String endDate,
                                     @Param("category") Integer category);

    ShopDayStats statsOrderAndRefundMoney(String shopId);
}
