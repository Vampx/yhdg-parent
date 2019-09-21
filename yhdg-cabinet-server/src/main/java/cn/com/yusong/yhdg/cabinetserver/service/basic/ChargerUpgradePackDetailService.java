package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.persistence.basic.ChargerUpgradePackDetailMapper;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class ChargerUpgradePackDetailService {
    @Autowired
    ChargerUpgradePackDetailMapper upgradePackChargerMapper;

    public ChargerUpgradePackDetail find(int upgradePackId, String chargerId) {
        return upgradePackChargerMapper.find(upgradePackId, chargerId);
    }

    public ChargerUpgradePackDetail findByTerminal(String chargerId) {
        return upgradePackChargerMapper.findByTerminal(chargerId);
    }

//    public List<ChargerUpgradePackDetail> findByUpgradePackId(int upgradePackId) {
//        return upgradePackChargerMapper.findByUpgradePackId(upgradePackId);
//    }
}
