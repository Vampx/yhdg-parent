package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.persistence.basic.BatteryUpgradePackMapper;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class BatteryUpgradePackService {
    @Autowired
    BatteryUpgradePackMapper batteryUpgradePackMapper;

    public BatteryUpgradePack find(int id) {
        return batteryUpgradePackMapper.find(id);
    }

    public List<BatteryUpgradePack> findByOldVersion(Integer packType, String oldVersion) {
        return batteryUpgradePackMapper.findByOldVersion(packType, oldVersion);
    }
}
