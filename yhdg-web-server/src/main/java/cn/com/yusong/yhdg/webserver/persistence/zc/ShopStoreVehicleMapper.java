package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ShopStoreVehicleMapper extends MasterMapper {
    ShopStoreVehicle find(@Param("id") long id);
    ShopStoreVehicle findByPriceSetting(@Param("shopId")String shopId,@Param("priceSettingId")long priceSettingId);
    ShopStoreVehicle findByVehicle(@Param("vehicleId")int vehicleId);
    ShopStoreVehicle findByVinNo(@Param("vinNo")String vinNo);
    List <ShopStoreVehicle> findByPriceSettingList(@Param("shopId")String shopId,@Param("priceSettingId")int priceSettingId);
    long findId();
    int findPageCount(ShopStoreVehicle search);
    List<ShopStoreVehicle> findPageResult(ShopStoreVehicle search);
    int insert(ShopStoreVehicle shopStoreBattery);
    int update(ShopStoreVehicle shopStoreVehicle);
    int updateBatteryCount(@Param("id") long id, @Param("batteryCount")int batteryCount);
    int delete(@Param("id") long id);
}
