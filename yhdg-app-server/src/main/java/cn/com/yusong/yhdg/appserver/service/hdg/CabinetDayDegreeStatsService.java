package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetDayDegreeStatsMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayDegreeStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CabinetDayDegreeStatsService extends AbstractService {
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;

    public CabinetDayDegreeStats findLast(String cabinetId) {
        return cabinetDayDegreeStatsMapper.findLast(cabinetId);
    }
}
