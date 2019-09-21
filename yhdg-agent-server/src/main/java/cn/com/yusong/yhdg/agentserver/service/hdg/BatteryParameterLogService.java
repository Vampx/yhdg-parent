package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameterLog;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryParameterLogService extends AbstractService {

    @Autowired
    BatteryParameterLogMapper batteryParameterLogMapper;

    public Page findPage(BatteryParameterLog search) {
        Page page = search.buildPage();
        page.setTotalItems(batteryParameterLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(batteryParameterLogMapper.findPageResult(search));
        return page;
    }

    public BatteryParameterLog find(Integer id) {
        return batteryParameterLogMapper.find(id);
    }

}
