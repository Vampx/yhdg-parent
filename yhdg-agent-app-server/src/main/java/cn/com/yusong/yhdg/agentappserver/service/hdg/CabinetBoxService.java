package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CabinetBoxService extends AbstractService {
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;

    public CabinetBox find(String cabinetId, String boxNum) {
        return cabinetBoxMapper.find(cabinetId, boxNum);
    }

    public List<CabinetBox> findByCabinetId(String cabinetId) {
        return cabinetBoxMapper.findByCabinetId(cabinetId);
    }

    public CabinetBox findByBatteryId(String batteryId) {
        return cabinetBoxMapper.findByBatteryId(batteryId);
    }


    public int updateBoxActive(String cabinetId, String boxNum, int isActive, String forbiddenCause, String operator, Date operatorTime) {
        return cabinetBoxMapper.updateBoxActive(cabinetId, boxNum, isActive, forbiddenCause, operator, operatorTime);
    }

    public int statsBoxCountByStatus(String cabinetId) {
        return cabinetBoxMapper.statsBoxCountByStatus(cabinetId);
    }

    public int statsCompleteChargeCount(String cabinetId, int batteryStatus) {
        return cabinetBoxMapper.statsCompleteChargeCount(cabinetId, batteryStatus);
    }
}
