package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChargerUpgradePackDetailMapper extends MasterMapper {

    int findPageCount(ChargerUpgradePackDetail chargerUpgradePackDetail);

    List<ChargerUpgradePackDetail> findPageResult(ChargerUpgradePackDetail chargerUpgradePackDetail);

    int insert(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);

    ChargerUpgradePackDetail find(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);

     int delete(@Param("upgradePackId") int upgradePackId, @Param("terminalId") String terminalId);
}
