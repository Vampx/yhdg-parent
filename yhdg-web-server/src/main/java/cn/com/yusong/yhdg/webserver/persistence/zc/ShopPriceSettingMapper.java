package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopPriceSettingMapper extends MasterMapper {
    ShopPriceSetting find(@Param("id") int id);
    int fondByShopCount(@Param("shopId") String shopId, @Param("priceSettingId") int priceSettingId);
    int findPageCount(ShopPriceSetting search);
    List<ShopPriceSetting> findPageResult(ShopPriceSetting search);
    int findByShopPageCount(ShopPriceSetting search);
    List<ShopPriceSetting> findByShopPageResult(ShopPriceSetting search);
    int insert(ShopPriceSetting shopStoreBattery);
    int delete(@Param("id") int id);
}
