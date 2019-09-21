package cn.com.yusong.yhdg.cabinetserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreVehicleBatteryMapper extends MasterMapper {
    int existBattery( @Param("batteryId") String batteryId);
}
