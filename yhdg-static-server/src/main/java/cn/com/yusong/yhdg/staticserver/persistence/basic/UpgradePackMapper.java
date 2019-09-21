package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface UpgradePackMapper extends MasterMapper {
    UpgradePack find(long id);
}
