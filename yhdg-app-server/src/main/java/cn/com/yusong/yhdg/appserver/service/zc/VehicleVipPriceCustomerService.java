package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.ShopStoreVehicleMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleVipPriceCustomerMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VehicleVipPriceCustomerService extends AbstractService {

    @Autowired
    VehicleVipPriceCustomerMapper vehicleVipPriceCustomerMapper;

    public VehicleVipPriceCustomer findByMobile(String mobile) {
        return vehicleVipPriceCustomerMapper.findByMobile(mobile);
    }

}
