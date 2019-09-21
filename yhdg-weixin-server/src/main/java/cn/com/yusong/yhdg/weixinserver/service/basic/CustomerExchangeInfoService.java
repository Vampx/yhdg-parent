package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.CustomerExchangeInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerExchangeInfoService {
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public CustomerExchangeInfo find(long id) {
        return customerExchangeInfoMapper.find(id);
    }
}
