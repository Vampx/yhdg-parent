package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetOnlineStatsMapper;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CabinetOnlineStatsService {
    @Autowired
    CabinetOnlineStatsMapper cabinetOnlineStatsMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public void end(String cabinetId) {
        cabinetOnlineStatsMapper.updateEndTime(cabinetId, new Date());
        cabinetMapper.updateOnline(cabinetId, ConstEnum.Flag.FALSE.getValue());
        cabinetBoxMapper.updateOnline(cabinetId, ConstEnum.Flag.FALSE.getValue());
        cabinetMapper.updateOperationFlag(cabinetId, ConstEnum.Flag.TRUE.getValue());
    }
}
