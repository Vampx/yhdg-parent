package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CabinetBoxService extends AbstractService {

    @Autowired
    CabinetBoxMapper cabinetBoxService;

    public List<CabinetBox> findAllEmpty(String cabinetId, int boxStatus) {
        return cabinetBoxService.findAllEmpty(cabinetId, boxStatus);
    }

    public List<CabinetBox> findByCabinet(String cabinetId) {
        return cabinetBoxService.findByCabinet(cabinetId);
    }

    public List<CabinetBox> findBySubcabinetId(String cabinetId) {
        return cabinetBoxService.findBySubcabinetId(cabinetId);
    }

    public List<CabinetBox> findAllNotFull(String cabinetId, Integer boxStatus) {
        return cabinetBoxService.findAllNotFull(cabinetId, boxStatus,Battery.Status.IN_BOX.getValue());
    }

    public List<CabinetBox> findList(String cabinetId ) {
        return cabinetBoxService.findList(cabinetId );
    }

    public CabinetBox findOneEmptyBoxNum(String cabinetId, int batteryType) {
        return super.findOneEmptyBoxNum(cabinetId, batteryType);
    }

    public CabinetBox findOneFull(String cabinetId, int batteryType, String bespeakBoxNum) {
        return cabinetBoxService.findOneFull(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId, null, batteryType, bespeakBoxNum);
    }

    public CabinetBox findOneFullByCabinet(String cabinetId) {
        return cabinetBoxService.findOneFullByCabinet(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId);
    }

    public CabinetBox find(String cabinetId, String boxNum) {
        return cabinetBoxService.find(cabinetId, boxNum);
    }

    public int findEmptyCount(String cabinetId) {
        return cabinetBoxService.findEmptyCount(cabinetId, CabinetBox.BoxStatus.EMPTY.getValue());
    }

    public int findFullCount(String cabinetId) {
        return cabinetBoxService.findFullCount(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinetId);
    }

    public int findBatteryCount(String cabinetId) {
        return cabinetBoxService.findBatteryCount(cabinetId);
    }

    public int lockBox(String cabinetId, String boxNum, int fromStatus, int toStatus) {
        return cabinetBoxService.lockBox(cabinetId, boxNum, fromStatus, toStatus, new Date());
    }

    public int unlockBox(String cabinetId, String boxNum, int fromStatus, int toStatus) {
        return cabinetBoxService.unlockBox(cabinetId, boxNum, fromStatus, toStatus);
    }

    public int updateOpenType(String cabinetId, String boxNum, int openType, long openerId) {
        return cabinetBoxService.updateOpenType(cabinetId, boxNum, openType, openerId);
    }
}
