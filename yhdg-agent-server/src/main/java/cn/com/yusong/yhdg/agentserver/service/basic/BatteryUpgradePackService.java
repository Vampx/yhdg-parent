package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.BatteryUpgradePackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
public class BatteryUpgradePackService {

    @Autowired
    BatteryUpgradePackMapper batteryUpgradePackMapper;

    @Autowired
    AppConfig appConfig;

    public BatteryUpgradePack find(long id){return batteryUpgradePackMapper.find(id);}

    public Page findPage(BatteryUpgradePack search){
        Page page = search.buildPage();
        page.setTotalItems(batteryUpgradePackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batteryUpgradePackMapper.findPageResult(search));
        return page;
    }

    public ExtResult insert(BatteryUpgradePack entity) {
        entity.setUpdateTime(new Date());
        batteryUpgradePackMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(BatteryUpgradePack entity) throws IOException {
        BatteryUpgradePack upgradePack = batteryUpgradePackMapper.find(entity.getId());
        if(upgradePack == null) {
            return ExtResult.failResult("记录不存在");
        }

        entity.setUpdateTime(new Date());
        batteryUpgradePackMapper.update(entity);

        return ExtResult.successResult();
    }

    public ExtResult delete(Integer id) {
        if (batteryUpgradePackMapper.delete(id) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
