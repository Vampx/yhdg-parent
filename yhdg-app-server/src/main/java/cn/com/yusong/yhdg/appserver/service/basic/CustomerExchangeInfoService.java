package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerExchangeInfoMapper;
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

    public int updateErrorMessage(long id, Date errorTime, String errorMessage) {
        return customerExchangeInfoMapper.updateErrorMessage(id, errorTime, errorMessage);
    }

    public int clearErrorMessage(long id) {
        return customerExchangeInfoMapper.updateErrorMessage(id, null, null);
    }

}
