package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeviceUpgradePackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
public class DeviceUpgradePackService {
    @Autowired
    private DeviceUpgradePackMapper deviceUpgradePackMapper;

    public DeviceUpgradePack find(long id) {
        return deviceUpgradePackMapper.find(id);
    }

    public Page findPage(DeviceUpgradePack search) {
        Page page = search.buildPage();
        page.setTotalItems(deviceUpgradePackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(deviceUpgradePackMapper.findPageResult(search));
        return page;
    }

    public ExtResult insert(DeviceUpgradePack entity) {
        entity.setUpdateTime(new Date());
        deviceUpgradePackMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(DeviceUpgradePack entity) {
        DeviceUpgradePack upgradePack = deviceUpgradePackMapper.find(entity.getId());
        if (upgradePack == null) {
            return ExtResult.failResult("记录不存在");
        }

        entity.setUpdateTime(new Date());
        deviceUpgradePackMapper.update(entity);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        if (deviceUpgradePackMapper.delete(id) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
