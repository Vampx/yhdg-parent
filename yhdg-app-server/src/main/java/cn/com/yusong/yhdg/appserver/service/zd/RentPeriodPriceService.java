package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentPeriodPriceService extends AbstractService {

    @Autowired
    RentPeriodPriceMapper rentPeriodPriceMapper;

    public RentPeriodPrice find(Long id) {
        return rentPeriodPriceMapper.find(id);
    }

    public List<RentPeriodPrice> findList(Integer agentId, Integer batteryType, Long foregiftId) {
        return rentPeriodPriceMapper.findList(agentId, batteryType, foregiftId);
    }

}
