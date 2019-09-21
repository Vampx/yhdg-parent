package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeWhiteListMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeWhiteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeWhiteListService extends AbstractService {

    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;


    public ExchangeWhiteList findByCustomer( Integer agentId, long customerId) {
        return exchangeWhiteListMapper.findByCustomer(agentId, customerId);
    }
}
