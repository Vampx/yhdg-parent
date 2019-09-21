package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleVipPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleVipPriceService extends AbstractService {

    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;

    public VehicleVipPrice findByRentPriceId(Long rentPriceId) {
        return vehicleVipPriceMapper.findByRentPriceId(rentPriceId);
    }

    public VehicleVipPrice find(Integer id) {
        return vehicleVipPriceMapper.find(id);
    }
}
