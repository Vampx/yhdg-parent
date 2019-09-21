package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.FaultLogMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetBoxMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CabinetBoxService extends AbstractService {
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    FaultLogMapper faultLogMapper;

    public void clearLockTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,-180);

        List<Integer> boxStatusList = new ArrayList<Integer>();
        boxStatusList.add(CabinetBox.BoxStatus.FULL_LOCK.getValue());
        boxStatusList.add(CabinetBox.BoxStatus.EMPTY_LOCK.getValue());

        List<CabinetBox> cabinetBoxes = cabinetBoxMapper.findLockList(calendar.getTime(), boxStatusList);

        for (CabinetBox cabinetBox : cabinetBoxes) {
            if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.FULL_LOCK.getValue()) {
                cabinetBoxMapper.updateStatus(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
            } else if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()) {
                cabinetBoxMapper.updateStatus(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
            }
        }
    }
}
