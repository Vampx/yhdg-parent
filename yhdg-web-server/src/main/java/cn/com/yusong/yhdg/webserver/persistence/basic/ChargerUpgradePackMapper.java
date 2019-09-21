package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ChargerUpgradePackMapper extends MasterMapper{

    public ChargerUpgradePack find(long id);
    public int findPageCount(ChargerUpgradePack search);
    public List<ChargerUpgradePack> findPageResult(ChargerUpgradePack search);
    public int insert(ChargerUpgradePack search);
    public int update(ChargerUpgradePack entity);
    public  int delete(long id);

}
