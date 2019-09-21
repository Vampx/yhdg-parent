package cn.com.yusong.yhdg.batteryserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface BatteryUpgradePackMapper extends MasterMapper {
    public BatteryUpgradePack find(int id);
    public List<BatteryUpgradePack> findByOldVersion(@Param("packType") Integer packType,  @Param("oldVersion") String version);
}
