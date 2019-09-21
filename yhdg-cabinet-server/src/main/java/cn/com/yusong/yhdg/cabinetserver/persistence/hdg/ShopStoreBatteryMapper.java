package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ShopStoreBatteryMapper extends MasterMapper {
    int findCount(@Param("shopId") String shopId);
    int findByBatteryId(@Param("batteryId") String batteryId);
    int findByShopBatteryId(@Param("shopId") String shopId, @Param("batteryId") String batteryId);
    int insert(ShopStoreBattery shopStoreBattery);
    int deleteByShopBatteryId(@Param("shopId") String shopId, @Param("batteryId") String batteryId);
}
