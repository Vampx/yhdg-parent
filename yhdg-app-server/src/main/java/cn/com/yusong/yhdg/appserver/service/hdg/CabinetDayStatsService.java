package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetDayStatsMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CabinetDayStatsService extends AbstractService {
    @Autowired
    CabinetDayStatsMapper cabinetDayStatsMapper;


    public CabinetDayStats findForCabinet(String cabinetId, String statsDate) {
        return cabinetDayStatsMapper.findForCabinet(cabinetId,statsDate);
    }


}
