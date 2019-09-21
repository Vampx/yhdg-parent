package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentCabinetForegiftRecordMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCabinetForegiftRecordService extends AbstractService{
    @Autowired
    AgentCabinetForegiftRecordMapper agentCabinetForegiftRecordMapper;

    public Page findPage(AgentCabinetForegiftRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(agentCabinetForegiftRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentCabinetForegiftRecord> agentCabinetForegiftRecordList = agentCabinetForegiftRecordMapper.findPageResult(search);
        for (AgentCabinetForegiftRecord agentCabinetForegiftRecord : agentCabinetForegiftRecordList) {
            AgentInfo agentInfo = findAgentInfo(agentCabinetForegiftRecord.getAgentId());
            if (agentInfo != null) {
                agentCabinetForegiftRecord.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(agentCabinetForegiftRecordList);
        return page;
    }
}
