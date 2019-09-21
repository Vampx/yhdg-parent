package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import cn.com.yusong.yhdg.staticserver.persistence.basic.UpgradePackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpgradePackService {
    @Autowired
    UpgradePackMapper upgradePackMapper;

    public UpgradePack find(long id){
        return  upgradePackMapper.find(id);
    }
}
