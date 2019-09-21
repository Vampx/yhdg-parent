package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopStoreVehicleMapper extends MasterMapper {
    ShopStoreVehicle findByVehicleId(@Param("vehicleId") Integer vehicleId);
    int clearVehicle(@Param("shopId") String shopId, @Param("vehicleId") Integer vehicleId);

    int findByVehicleCount(@Param("shopId") String shopId, @Param("settingId") Integer settingId);

}
