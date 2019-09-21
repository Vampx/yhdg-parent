package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerDepositGiftMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDepositGiftService {
    @Autowired
    CustomerDepositGiftMapper customerDepositGiftMapper;

    public List<CustomerDepositGift> findAll(Integer appId) {
        return customerDepositGiftMapper.findAll(appId);
    }
}
