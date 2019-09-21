package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalUpgradePackDetailMapper extends MasterMapper {

    int findPageCount(TerminalUpgradePackDetail terminalUpgradePackDetail);

    List<TerminalUpgradePackDetail> findPageResult(TerminalUpgradePackDetail terminalUpgradePackDetail);

    int findPageScreenCount(TerminalUpgradePackDetail terminalUpgradePackDetail);

    List<TerminalUpgradePackDetail> findPageScreenResult(TerminalUpgradePackDetail terminalUpgradePackDetail);

    int insert(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);

    TerminalUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);

     int delete(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);
}
