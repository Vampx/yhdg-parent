package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
public interface ChargerUpgradePackMapper extends MasterMapper {
    public ChargerUpgradePack find(int id);
    public List<ChargerUpgradePack> findByOldVersion(@Param("packType") int packType, @Param("oldVersion") String version);
}
