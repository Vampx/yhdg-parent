package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface DeviceUpgradePackMapper extends MasterMapper {

    public DeviceUpgradePack findByDevice (@Param("deviceType") int deviceType, @Param("oldVersion")String oldVersion, @Param("deviceId")String deviceId);
}
