package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface ChargerUpgradePackDetailMapper extends MasterMapper {
    public ChargerUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);

    public ChargerUpgradePackDetail findByTerminal(@Param("terminalId") String terminalId);

    public List<ChargerUpgradePackDetail> findByUpgradePackId(int upgradePackId);
}
