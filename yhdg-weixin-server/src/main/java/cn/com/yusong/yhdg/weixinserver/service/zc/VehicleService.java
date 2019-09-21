package cn.com.yusong.yhdg.weixinserver.service.zc;

import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.weixinserver.persistence.zc.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    VehicleMapper vehicleMapper;

    public Vehicle findByVinNo(String vinNo) {
        return vehicleMapper.findByVinNo(vinNo);
    }
}
