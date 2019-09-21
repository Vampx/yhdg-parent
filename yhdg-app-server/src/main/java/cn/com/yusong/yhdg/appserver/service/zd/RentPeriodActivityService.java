package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodActivityMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentPeriodActivityService extends AbstractService {

    @Autowired
    RentPeriodActivityMapper rentPeriodActivityMapper;

    public List<RentPeriodActivity> findList(Integer agentId, Integer batteryType) {
        return rentPeriodActivityMapper.findList(agentId, batteryType);
    }
}
