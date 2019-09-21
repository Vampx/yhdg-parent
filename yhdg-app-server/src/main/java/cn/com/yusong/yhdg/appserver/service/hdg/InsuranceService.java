package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.InsuranceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceService extends AbstractService {

    @Autowired
    InsuranceMapper insuranceMapper;

    public Insurance find(long id) {
        return insuranceMapper.find(id);
    }

    public List<Insurance> findList(Integer agentId, Integer batteryType) {
        return insuranceMapper.findList(agentId, batteryType);
    }
}
