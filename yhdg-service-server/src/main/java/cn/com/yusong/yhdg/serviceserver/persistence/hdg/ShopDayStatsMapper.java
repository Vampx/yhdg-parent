package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ShopDayStatsMapper extends MasterMapper {
    public ShopDayStats find(@Param("shopId") String shopId, @Param("statsDate") String statsDate , @Param("category") Integer category);
    public ShopTotalStats sumTotal(@Param("shopId") String shopId , @Param("category") Integer category);
    public int insert(ShopDayStats stats);
    public int update(ShopDayStats stats);
}
