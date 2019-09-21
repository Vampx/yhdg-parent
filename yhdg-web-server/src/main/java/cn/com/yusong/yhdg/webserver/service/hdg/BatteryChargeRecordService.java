package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryChargeRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryChargeRecordMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatteryChargeRecordService extends AbstractService {

    @Autowired
    BatteryChargeRecordMapper batteryChargeRecordMapper;

    public BatteryChargeRecord find(long id) {
        return batteryChargeRecordMapper.find(id);
    }

    public Page findPage(BatteryChargeRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryChargeRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryChargeRecord> batteryChargeRecordList = batteryChargeRecordMapper.findPageResult(search);
        for (BatteryChargeRecord batteryChargeRecord: batteryChargeRecordList) {
            batteryChargeRecord.setTypeName(BatteryChargeRecord.Type.getName(batteryChargeRecord.getType()));
            batteryChargeRecord.setAgentName(findAgentInfo(batteryChargeRecord.getAgentId()).getAgentName());
        }
        page.setResult(batteryChargeRecordList);
        return page;
    }
}
