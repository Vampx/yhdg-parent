package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeviceUpgradePackDetailMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceUpgradePackDetailService extends AbstractService {
    @Autowired
    DeviceUpgradePackDetailMapper deviceUpgradePackDetailMapper;

    public Page findPage(DeviceUpgradePackDetail deviceUpgradePackDetail) {
        Page page = deviceUpgradePackDetail.buildPage();
        page.setTotalItems(deviceUpgradePackDetailMapper.findPageCount(deviceUpgradePackDetail));
        deviceUpgradePackDetail.setBeginIndex(page.getOffset());
        page.setResult(deviceUpgradePackDetailMapper.findPageResult(deviceUpgradePackDetail));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(int upgradePackId, String[] terminalId) {
        for (String id : terminalId) {
            DeviceUpgradePackDetail packDetail = deviceUpgradePackDetailMapper.find(upgradePackId, id);
            if (packDetail != null) {
                return ExtResult.failResult("编号已存在");
            }

            deviceUpgradePackDetailMapper.insert(upgradePackId, id);
        }

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int upgradePackId, String terminalId) {
        int result = deviceUpgradePackDetailMapper.delete(upgradePackId, terminalId);
        if (result == 0) {
            return ExtResult.failResult("删除失败！");
        }

        return ExtResult.successResult();
    }

}
