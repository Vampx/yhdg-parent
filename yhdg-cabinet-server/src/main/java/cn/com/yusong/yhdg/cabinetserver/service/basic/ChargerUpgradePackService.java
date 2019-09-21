package cn.com.yusong.yhdg.cabinetserver.service.basic;

import cn.com.yusong.yhdg.cabinetserver.persistence.basic.ChargerUpgradePackMapper;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chen on 2017/5/17.
 */
@Service
public class ChargerUpgradePackService {
    @Autowired
    ChargerUpgradePackMapper chargerUpgradePackMapper;

    public ChargerUpgradePack find(int id) {
        return chargerUpgradePackMapper.find(id);
    }

    public List<ChargerUpgradePack> findByOldVersion(int packType, String oldVersion) {
        return chargerUpgradePackMapper.findByOldVersion(packType, oldVersion);
    }
}
