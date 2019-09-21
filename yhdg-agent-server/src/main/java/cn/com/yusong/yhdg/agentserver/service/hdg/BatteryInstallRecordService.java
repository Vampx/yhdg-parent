package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryInstallRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryInstallRecordMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryInstallRecordService extends AbstractService {

    @Autowired
    BatteryInstallRecordMapper batteryInstallRecordMapper;

    public BatteryInstallRecord find(int id) {
        return batteryInstallRecordMapper.find(id);
    }

    public Page findPage(BatteryInstallRecord entity) {
        Page page = entity.buildPage();
        page.setTotalItems(batteryInstallRecordMapper.findPageCount(entity));
        entity.setBeginIndex(page.getOffset());
        page.setResult(batteryInstallRecordMapper.findPageResult(entity));
        return page;
    }

    public BatteryInstallRecord findByBatteryType(Integer batteryType, Integer agentId) {
        return batteryInstallRecordMapper.findByBatteryType(batteryType, agentId);
    }

    public int insert(BatteryInstallRecord batteryInstallRecord) {
        return batteryInstallRecordMapper.insert(batteryInstallRecord);
    }

    public int update(int id, int status) {
        return batteryInstallRecordMapper.update(id, status);
    }
}
