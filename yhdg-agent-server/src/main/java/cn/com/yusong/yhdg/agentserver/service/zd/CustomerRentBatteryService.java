package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRentBatteryService {

    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;

    public List<CustomerRentBattery> findByCustomerId(Long customerId) {
        return customerRentBatteryMapper.findByCustomerId(customerId);
    }
}
