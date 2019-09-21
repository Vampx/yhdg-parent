package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface DeviceUpgradePackMapper extends MasterMapper {

    DeviceUpgradePack find(long id);

    int findPageCount(DeviceUpgradePack search);

    List<DeviceUpgradePack> findPageResult(DeviceUpgradePack search);

    int insert(DeviceUpgradePack search);

    int update(DeviceUpgradePack entity);

    int delete(long id);

}
