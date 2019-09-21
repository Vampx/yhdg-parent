package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface TerminalUpgradePackMapper extends MasterMapper {
    public TerminalUpgradePack find(int id);
    public List<TerminalUpgradePack> findByOldVersion(@Param("packType") int packType, @Param("oldVersion") String version);
}
