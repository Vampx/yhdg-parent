package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

/**
 * Created by chen on 2017/10/31.
 */
public interface UpgradePackMapper extends MasterMapper{
    public UpgradePack find(long id);
    public int findPageCount(UpgradePack search);
    public List<UpgradePack> findPageResult(UpgradePack search);
    public int findScreenPageCount(UpgradePack search);
    public List<UpgradePack> findScreenPageResult(UpgradePack search);
    public int update(UpgradePack entity);
}
