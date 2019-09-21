package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRentBatteryService {

    @Autowired
    CustomerRentBatteryMapper customerRentBatteryMapper;

    public List<CustomerRentBattery> findByCustomerId(long customerId) {
      return customerRentBatteryMapper.findByCustomerId(customerId);
    }
}
