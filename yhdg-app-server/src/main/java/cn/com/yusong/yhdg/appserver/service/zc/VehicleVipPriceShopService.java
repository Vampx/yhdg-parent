package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleVipPriceMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleVipPriceShopMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleVipPriceShopService extends AbstractService {

    @Autowired
    VehicleVipPriceShopMapper vehicleVipPriceShopMapper;

    public VehicleVipPriceShop findByShopId(String shopId) {
        return vehicleVipPriceShopMapper.findByShopId(shopId);
    }
}
