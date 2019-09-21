package cn.com.yusong.yhdg.webserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleVipPriceMapper extends MasterMapper {
    int findCountByModelId(@Param("modelId") Integer modelId);
    VehicleVipPrice find(@Param("id") Integer id);
    VehicleVipPrice findByRentPriceId(@Param("rentPriceId") Integer rentPriceId);
    int findPageCount(VehicleVipPrice vehicleVipPrice);
    List<VehicleVipPrice> findPageResult(VehicleVipPrice vehicleVipPrice);
    int updateCustomerCount(@Param("id") Long id, @Param("customerCount") Integer customerCount);
    int updateShopCount(@Param("id") Long id, @Param("shopCount") Integer shopCount);
    int insert(VehicleVipPrice vehicleVipPrice);
    int update(VehicleVipPrice vehicleVipPrice);
    int delete(@Param("id") Integer id);
}
