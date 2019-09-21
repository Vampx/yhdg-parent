package cn.com.yusong.yhdg.agentappserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceShop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPriceShopMapper extends MasterMapper {
    List<VipRentPriceShop> findListByPriceId(@Param("priceId") long priceId);
    VipRentPriceShop findByPriceId(@Param("priceId") long priceId, @Param("shopId") String shopId);
    int insert(VipRentPriceShop vipRentPriceShop);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
