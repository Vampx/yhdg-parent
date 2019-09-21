package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.UpgradePackMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.UpgradePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpgradePackService extends AbstractService {
    @Autowired
    UpgradePackMapper upgradePackMapper;

    public UpgradePack find(int id){
        return  upgradePackMapper.find(id);
    }
}
