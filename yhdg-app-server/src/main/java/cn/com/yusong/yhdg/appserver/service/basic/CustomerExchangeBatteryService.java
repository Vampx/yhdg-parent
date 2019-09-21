package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerExchangeBatteryService {

    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;

    public List<CustomerExchangeBattery> findListByCustomer(long customerId) {
        return customerExchangeBatteryMapper.findListByCustomer(customerId);
    }

    public int exists(Long customerId) {
        return customerExchangeBatteryMapper.exists(customerId);
    }
}
