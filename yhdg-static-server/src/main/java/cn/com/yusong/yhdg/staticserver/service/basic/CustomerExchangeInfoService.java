package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.staticserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerExchangeInfoService {
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public CustomerExchangeInfo find(long id) {
        return customerExchangeInfoMapper.find(id);
    }
}
