package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerAgentBalance;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerAgentBalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAgentBalanceService {

    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;

    public CustomerAgentBalance findByCustomerId(Long customerId) {
        return customerAgentBalanceMapper.findByCustomerId(customerId);
    }
}
