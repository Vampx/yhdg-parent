package cn.com.yusong.yhdg.webserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceShop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleVipPriceShopMapper extends MasterMapper {
    List<VehicleVipPriceShop> findListByPriceId(@Param("priceId") long priceId);
    VehicleVipPriceShop findByShopId(@Param("shopId") String shopId);
    VehicleVipPriceShop find(@Param("id") long id);
    VehicleVipPriceShop findByPriceId(@Param("priceId") long priceId, @Param("shopId") String shopId);
    int insert(VehicleVipPriceShop vehicleVipPriceShop);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
