package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.BatteryUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.BatteryUpgradePackDetailMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatteryUpgradePackDetailService extends AbstractService {

    @Autowired
    BatteryUpgradePackDetailMapper batteryUpgradePackDetailMapper;

    public Page findPage(BatteryUpgradePackDetail batteryUpgradePackDetail) {
        Page page = batteryUpgradePackDetail.buildPage();
        page.setTotalItems(batteryUpgradePackDetailMapper.findPageCount(batteryUpgradePackDetail));
        batteryUpgradePackDetail.setBeginIndex(page.getOffset());
        page.setResult(batteryUpgradePackDetailMapper.findPageResult(batteryUpgradePackDetail));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(int upgradePackId, String[] batteryId) {

        for (int a = 0; a < batteryId.length; a++) {
            BatteryUpgradePackDetail batteryUpgradePackDetail = batteryUpgradePackDetailMapper.find(upgradePackId, batteryId[a]);
            if (null == batteryUpgradePackDetail) {
                batteryUpgradePackDetailMapper.insert(upgradePackId, batteryId[a]);
            } else {
                continue;
                //return ExtResult.failResult("不可重复添加!");
            }
        }
        return ExtResult.successResult();

    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(int upgradePackId, String batteryId) {
        int result = batteryUpgradePackDetailMapper.delete(upgradePackId, batteryId);
        if (result == 0) {
            return ExtResult.failResult("删除失败！");
        }
        return ExtResult.successResult();
    }

}
