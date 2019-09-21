package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceUpgradePackDetailMapper extends MasterMapper {

    int findPageCount(DeviceUpgradePackDetail upgradePackDetail);

    List<DeviceUpgradePackDetail> findPageResult(DeviceUpgradePackDetail upgradePackDetail);

    int insert(@Param("packId") int packId, @Param("deviceId") String terminalId);

    DeviceUpgradePackDetail find(@Param("packId") int packId, @Param("deviceId") String terminalId);

    int delete(@Param("packId") int packId, @Param("deviceId") String terminalId);
}
