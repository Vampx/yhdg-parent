package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangePriceTimeMapper;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangePriceTimeService {

    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;

    public ExchangePriceTime findByBatteryType(int agentId,  int batteryType) {
        return exchangePriceTimeMapper.findByBatteryType(agentId, batteryType);
    }

}
