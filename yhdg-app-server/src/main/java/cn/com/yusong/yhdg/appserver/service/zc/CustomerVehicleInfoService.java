package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.CustomerVehicleInfoMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerVehicleInfoService extends AbstractService {

    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;

    public CustomerVehicleInfo findByCustomerId(Long customerId) {
        return customerVehicleInfoMapper.findByCustomerId(customerId);
    }
}