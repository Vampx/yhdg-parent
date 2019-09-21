package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetBoxService {
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public List<CabinetBox> findBoxBatteryList(String subcabinetId) {
        return cabinetBoxMapper.findBoxBatteryList(subcabinetId);
    }

    public List<CabinetBox> findListByCabinet(String cabinetId) {
        return cabinetBoxMapper.findListByCabinet(cabinetId);
    }
}
