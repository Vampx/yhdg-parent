package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalUpgradePackMapper extends MasterMapper{

    public TerminalUpgradePack find(long id);
    public int findPageCount(TerminalUpgradePack search);
    public List<TerminalUpgradePack> findPageResult(TerminalUpgradePack search);
    public int insert(TerminalUpgradePack search);
    public int update(TerminalUpgradePack entity);
    public  int delete(long id);

}
