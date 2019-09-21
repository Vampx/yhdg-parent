package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeBatteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerExchangeBatteryService {

    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;

    public List<CustomerExchangeBattery> findByCustomerId(Long customerId) {
        return customerExchangeBatteryMapper.findByCustomerId(customerId);
    }
}
