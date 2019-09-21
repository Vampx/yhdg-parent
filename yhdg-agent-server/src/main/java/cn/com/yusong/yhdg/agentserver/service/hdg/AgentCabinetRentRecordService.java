package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.AgentCabinetRentRecordMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCabinetRentRecordService extends AbstractService{
    @Autowired
    AgentCabinetRentRecordMapper agentCabinetRentRecordMapper;

    public Page findPage(AgentCabinetRentRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(agentCabinetRentRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentCabinetRentRecord> agentCabinetRentRecordList = agentCabinetRentRecordMapper.findPageResult(search);
        for (AgentCabinetRentRecord agentCabinetRentRecord : agentCabinetRentRecordList) {
            AgentInfo agentInfo = findAgentInfo(agentCabinetRentRecord.getAgentId());
            if (agentInfo != null) {
                agentCabinetRentRecord.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(agentCabinetRentRecordList);
        return page;
    }
}
