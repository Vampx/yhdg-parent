package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceShopMapper extends MasterMapper {
    List<VipPriceShop> findListByPriceId(@Param("priceId") long priceId);
    VipPriceShop findByPriceId(@Param("priceId") long priceId, @Param("shopId") String shopId);
    int insert(VipPriceShop vipPriceShop);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
