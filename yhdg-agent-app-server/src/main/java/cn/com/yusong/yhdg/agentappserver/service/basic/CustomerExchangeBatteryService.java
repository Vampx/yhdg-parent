
package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerExchangeBatteryService extends AbstractService {
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;

    public CustomerExchangeBattery findByBatteryId(String batteryId) {
        return customerExchangeBatteryMapper.findByBatteryId(batteryId);
    }

    public List<CustomerExchangeBattery> findByCustomerId(long customerId) {
        return customerExchangeBatteryMapper.findByCustomerId(customerId);
    }
}


