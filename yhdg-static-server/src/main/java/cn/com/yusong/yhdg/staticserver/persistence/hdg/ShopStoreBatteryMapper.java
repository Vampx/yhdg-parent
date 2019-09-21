package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface ShopStoreBatteryMapper extends MasterMapper {
    public ShopStoreBattery findByBattery(@Param("batteryId") String batteryId);

    int clearBattery(@Param("shopId") String shopId, @Param("batteryId") String battertId);
}
