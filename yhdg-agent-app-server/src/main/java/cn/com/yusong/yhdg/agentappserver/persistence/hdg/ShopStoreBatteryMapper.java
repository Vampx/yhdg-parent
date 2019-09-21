package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreBatteryMapper extends MasterMapper {
    int findCount(@Param("shopId")String shopId);
    ShopStoreBattery findByBattery(@Param("batteryId") String batteryId);
    int findByBatteryId(@Param("batteryId")String batteryId);
    int findByShopBatteryId(@Param("shopId")String shopId, @Param("batteryId")String batteryId);
    int insert(ShopStoreBattery shopStoreBattery);
    int clearBattery(@Param("shopId")String shopId, @Param("batteryId") String batteryId);
    int deleteByShopBatteryId(@Param("shopId")String shopId, @Param("batteryId")String batteryId);
}
