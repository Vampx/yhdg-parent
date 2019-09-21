package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.ShopStoreVehicleMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShopStoreVehicleService extends AbstractService {

    @Autowired
    ShopStoreVehicleMapper shopStoreVehicleMapper;

    public ShopStoreVehicle findByVehicleId(Integer vehicleId) {
        return shopStoreVehicleMapper.findByVehicleId(vehicleId);
    }

    public int  findByVehicleCount(String shopId,Integer settingId) {
        return shopStoreVehicleMapper.findByVehicleCount(shopId,settingId);
    }
}
