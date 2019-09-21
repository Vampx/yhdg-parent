package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreBatteryMapper extends MasterMapper {
    public ShopStoreBattery findByBattery(@Param("batteryId") String batteryId);

    int clearBattery(@Param("shopId") String shopId, @Param("batteryId") String battertId);

    public List<ShopStoreBattery> findByShopId(@Param("shopId") String shopId, @Param("category") Integer category);

    int insert(ShopStoreBattery shopStoreBattery);
}
