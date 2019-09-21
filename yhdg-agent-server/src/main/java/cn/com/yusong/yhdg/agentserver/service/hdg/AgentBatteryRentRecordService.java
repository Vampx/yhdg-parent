package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.AgentBatteryRentRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.AgentBatteryRentRecordMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentBatteryRentRecordService extends AbstractService{
    @Autowired
    AgentBatteryRentRecordMapper agentbatteryrentrecordMapper;

    public Page findPage(AgentBatteryRentRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(agentbatteryrentrecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentBatteryRentRecord> agentBatteryRentRecordList = agentbatteryrentrecordMapper.findPageResult(search);
        for (AgentBatteryRentRecord agentBatteryRentRecord : agentBatteryRentRecordList) {
            AgentInfo agentInfo = findAgentInfo(agentBatteryRentRecord.getAgentId());
            if (agentInfo != null) {
                agentBatteryRentRecord.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(agentBatteryRentRecordList);
        return page;
    }
}
