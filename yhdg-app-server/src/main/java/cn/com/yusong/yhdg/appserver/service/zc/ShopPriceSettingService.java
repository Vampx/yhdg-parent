package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.ShopPriceSettingMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopPriceSettingService extends AbstractService {

    @Autowired
    ShopPriceSettingMapper shopPriceSettingMapper;

    public List<ShopPriceSetting> findByPriceSettingIdAll(Long priceSettingId){
       return shopPriceSettingMapper.findByPriceSettingIdAll(priceSettingId);
    }
    public List<ShopPriceSetting> findByPriceListShop(String shopId ,Long priceSettingId){
        return shopPriceSettingMapper.findByPriceListShop(shopId,priceSettingId);
    }
}
