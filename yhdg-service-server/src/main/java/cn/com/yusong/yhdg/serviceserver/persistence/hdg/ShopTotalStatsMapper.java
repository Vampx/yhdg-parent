package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetTotalStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopTotalStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ShopTotalStatsMapper extends MasterMapper {
    public ShopTotalStats find(@Param("shopId") String shopId, @Param("category") Integer category);
    public int insert(ShopTotalStats stats);
    public int update(ShopTotalStats stats);
}
