package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.persistence.basic.TerminalUpgradePackDetailMapper;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class TerminalUpgradePackDetailService {
    @Autowired
    TerminalUpgradePackDetailMapper upgradePackChargerMapper;

    public TerminalUpgradePackDetail find(int upgradePackId, String chargerId) {
        return upgradePackChargerMapper.find(upgradePackId, chargerId);
    }

//    public List<TerminalUpgradePackDetail> findByUpgradePackId(int upgradePackId) {
//        return upgradePackChargerMapper.findByUpgradePackId(upgradePackId);
//    }
}
