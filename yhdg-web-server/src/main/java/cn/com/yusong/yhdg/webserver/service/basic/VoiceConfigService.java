package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.VoiceConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoiceConfigService {
    @Autowired
    private VoiceConfigMapper voiceConfigMapper;
    @Autowired
    private AgentMapper agentMapper;

    public VoiceConfig find(int id) {
        return voiceConfigMapper.find(id);
    }

    public Page findPage(VoiceConfig search) {
        Page page = search.buildPage();
        page.setTotalItems(voiceConfigMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(voiceConfigMapper.findPageResult(search));
        return page;
    }

    public int insert(VoiceConfig entity) {
        Agent agent = agentMapper.find(entity.getAgentId());
        entity.setAgentName(agent.getAgentName());
        entity.setAgentCode(agent.getAgentCode());
        entity.setConfigName(VoiceConfig.Type.getName(entity.getSmsType()));
        return voiceConfigMapper.insert(entity);
    }

    public int update(VoiceConfig entity) {
        return voiceConfigMapper.update(entity);
    }

}
