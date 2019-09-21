package cn.com.yusong.yhdg.batteryserver.service.basic;

import cn.com.yusong.yhdg.batteryserver.persistence.basic.UpgradePackDetailMapper;
import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class BatteryUpgradePackDetailService {
    @Autowired
    UpgradePackDetailMapper upgradePackDetailMapper;

    public BatteryUpgradePackDetail find(int upgradePackId, String batteryId) {
        return upgradePackDetailMapper.find(upgradePackId, batteryId);
    }
}
