package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentInsuranceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentInsuranceService extends AbstractService {

    @Autowired
    RentInsuranceMapper insuranceMapper;

    public RentInsurance find(long id) {
        return insuranceMapper.find(id);
    }

    public List<RentInsurance> findList(Integer agentId, Integer batteryType) {
        return insuranceMapper.findList(agentId, batteryType);
    }
}
