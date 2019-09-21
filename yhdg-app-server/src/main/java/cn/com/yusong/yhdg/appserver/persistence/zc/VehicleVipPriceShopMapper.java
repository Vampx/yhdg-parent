package cn.com.yusong.yhdg.appserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceShop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface VehicleVipPriceShopMapper extends MasterMapper {
    public VehicleVipPriceShop findByShopId(@Param("shopId") String shopId);
}
