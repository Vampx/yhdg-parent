package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface UpgradePackMapper extends MasterMapper {
    public UpgradePack find(@Param("id") int id);
}
