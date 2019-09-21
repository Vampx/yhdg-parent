package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface TerminalUpgradePackDetailMapper extends MasterMapper {
    public TerminalUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String chargerId);

    public List<TerminalUpgradePackDetail> findByUpgradePackId(int upgradePackId);
}
