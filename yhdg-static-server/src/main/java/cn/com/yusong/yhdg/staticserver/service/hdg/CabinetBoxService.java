package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.staticserver.service.basic.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class CabinetBoxService extends AbstractService {
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;


    public  CabinetBox findOneFull(String cabinetId, String batteryId, int batteryType, String bespeakBoxNum) {
        return cabinetBoxMapper.findOneFull( CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId, batteryId, batteryType, bespeakBoxNum);
    }

    public   CabinetBox find(String cabinetId, String boxNum) {
        return cabinetBoxMapper.find(cabinetId, boxNum);
    }

    public int findFullCount(String cabinetId) {
        return cabinetBoxMapper.findFullCount(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId);
    }

    public int lockBox(String cabinetId, String boxNum, int fromStatus, int toStatus) {
        return cabinetBoxMapper.lockBox(cabinetId, boxNum, fromStatus, toStatus, new Date());
    }

    public int unlockBox(String cabinetId, String boxNum, int fromStatus, int toStatus) {
        return cabinetBoxMapper.unlockBox(cabinetId, boxNum, fromStatus, toStatus);
    }

}
