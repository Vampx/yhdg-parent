package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.DeviceUpgradePackMapper;
import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceUpgradePackService {
    @Autowired
    DeviceUpgradePackMapper deviceUpgradePackMapper;

    public DeviceUpgradePack findByDevice (int deviceType, String  oldVersion, String deviceId) {
        return deviceUpgradePackMapper.findByDevice(deviceType, oldVersion, deviceId);
    }
}
