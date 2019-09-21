package cn.com.yusong.yhdg.agentserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopInventoryBatteryMapper extends MasterMapper {
    public ShopStoreBattery findByBattery(@Param("batteryId") String batteryId);
    public List<ShopStoreBattery> findByShopId(@Param("shopId") String shopId);
    public int findPageCount(ShopStoreBattery search);
    ShopStoreBattery find(@Param("id") long id);
    public List<ShopStoreBattery> findPageResult(ShopStoreBattery search);
    int insert(ShopStoreBattery shopStoreBattery);
    int deleteByShopBatteryId(@Param("shopId") String shopId, @Param("batteryId") String batteryId);
    int delete(@Param("id") long id);
}
