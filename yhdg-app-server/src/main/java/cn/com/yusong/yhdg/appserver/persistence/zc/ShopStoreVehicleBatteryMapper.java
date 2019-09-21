package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreVehicleBatteryMapper extends MasterMapper {
    List<ShopStoreVehicleBattery> findByStoreVehicle(@Param("storeVehicleId") Long storeVehicleId);
    int existBattery( @Param("batteryId") String batteryId);
    int clearVehicleBattery(@Param("storeVehicleId") Long storeVehicleId);

}
