package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerExchangeInfoService {
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public CustomerExchangeInfo findByCustomerId(Long customerId) {
        return customerExchangeInfoMapper.find(customerId);
    }
}
