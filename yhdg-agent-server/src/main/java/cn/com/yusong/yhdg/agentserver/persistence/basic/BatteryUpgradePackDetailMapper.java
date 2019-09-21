package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryUpgradePackDetailMapper extends MasterMapper {

    int findPageCount(BatteryUpgradePackDetail batteryUpgradePackDetail);

    List<BatteryUpgradePackDetail> findPageResult(BatteryUpgradePackDetail batteryUpgradePackDetail);

    int insert(@Param("upgradePackId") int upgradePackId, @Param("batteryId") String batteryId);

    BatteryUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("batteryId") String batteryId);

     int delete(@Param("upgradePackId") int upgradePackId, @Param("batteryId") String batteryId);
}
