package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleModelService extends AbstractService {

    @Autowired
    VehicleModelMapper vehicleModelMapper;

    public VehicleModel find(Integer id) {
        return vehicleModelMapper.find(id);
    }
}
