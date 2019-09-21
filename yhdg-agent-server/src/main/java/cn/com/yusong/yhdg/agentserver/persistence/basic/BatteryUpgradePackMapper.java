package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface BatteryUpgradePackMapper extends MasterMapper{

    public BatteryUpgradePack find(long id);
    public int findPageCount(BatteryUpgradePack search);
    public List<BatteryUpgradePack> findPageResult(BatteryUpgradePack search);
    public int insert(BatteryUpgradePack search);
    public int update(BatteryUpgradePack entity);
    public  int delete(int id);

}
