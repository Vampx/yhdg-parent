package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentPublicNoticeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AgentPublicNoticeService extends AbstractService {

    @Autowired
    AgentPublicNoticeMapper agentPublicNoticeMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    public AgentPublicNotice find(long id) {
        return agentPublicNoticeMapper.find(id);
    }

    public Page findPage(AgentPublicNotice search) {
        Page page = search.buildPage();
        page.setTotalItems(agentPublicNoticeMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentPublicNotice> list = agentPublicNoticeMapper.findPageResult(search);
        for (AgentPublicNotice agentPublicNotice : list) {
            agentPublicNotice.setAgentName(findAgentInfo(agentPublicNotice.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(AgentPublicNotice agentPublicNotice) {
        agentPublicNotice.setCreateTime(new Date());
        agentPublicNoticeMapper.insert(agentPublicNotice);
        return ExtResult.successResult();
    }

    public ExtResult update(AgentPublicNotice agentPublicNotice) {
        agentPublicNoticeMapper.update(agentPublicNotice);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        agentPublicNoticeMapper.delete(id);
        pushMetaDataMapper.delete(String.valueOf(id));
        return ExtResult.successResult();
    }

    public int pushMetaDataCreate(String sourceId,Integer sourceType) {
        PushMetaData pushMetaData = new PushMetaData();
        pushMetaData.setSourceId(sourceId);
        pushMetaData.setSourceType(sourceType);
        pushMetaData.setCreateTime(new Date());
        return pushMetaDataMapper.insert(pushMetaData);
    }
}
