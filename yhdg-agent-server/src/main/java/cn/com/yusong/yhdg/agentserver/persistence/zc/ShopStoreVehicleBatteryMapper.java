package cn.com.yusong.yhdg.agentserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreVehicleBatteryMapper extends MasterMapper {
    ShopStoreVehicleBattery find(@Param("storeVehicleId") int storeVehicleId, @Param("batteryId") String batteryId);
    List<ShopStoreVehicleBattery> findByStoreVehicle(@Param("storeVehicleId") Long storeVehicleId);
    ShopStoreVehicleBattery findByBatteryId(@Param("batteryId") String batteryId);
    int insert(ShopStoreVehicleBattery shopStoreVehicleBattery);
    int delete(@Param("storeVehicleId") long storeVehicleId, @Param("batteryId") String batteryId);
    int deleteByStoreVehicle(@Param("storeVehicleId") long storeVehicleId);
}
