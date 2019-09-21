package cn.com.yusong.yhdg.batteryserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface UpgradePackDetailMapper extends MasterMapper {
    public BatteryUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("batteryId") String batteryId);


}
