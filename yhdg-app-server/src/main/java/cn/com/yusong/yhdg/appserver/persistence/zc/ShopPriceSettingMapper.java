package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopPriceSettingMapper extends MasterMapper {
    ShopPriceSetting find(@Param("id") int id);

    int fondByShopCount(@Param("shopId") String shopId, @Param("priceSettingId") int priceSettingId);

    List<ShopPriceSetting> findByPriceSettingIdAll(@Param("priceSettingId") Long priceSettingId);

    List<ShopPriceSetting> findByPriceListShop(@Param("shopId") String shopId,@Param("priceSettingId") Long priceSettingId);
}
