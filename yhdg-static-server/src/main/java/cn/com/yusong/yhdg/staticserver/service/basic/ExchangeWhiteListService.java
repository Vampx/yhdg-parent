package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.staticserver.persistence.hdg.ExchangeWhiteListMapper;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeWhiteListService extends AbstractService {

    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;


    public ExchangeWhiteList findByCustomer( int agentId, long customerId) {
        return exchangeWhiteListMapper.findByCustomer(agentId, customerId);
    }
}
