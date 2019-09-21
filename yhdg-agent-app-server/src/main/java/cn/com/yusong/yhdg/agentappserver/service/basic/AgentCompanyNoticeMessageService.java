package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCompanyNoticeMessageService {
    @Autowired
    AgentCompanyNoticeMessageMapper agentCompanyNoticeMessageMapper;
    @Autowired
    AgentCompanyPublicNoticeMapper agentCompanyPublicNoticeMapper;
    @Autowired
    PublicNoticeMapper publicNoticeMapper;


    public List<AgentCompanyNoticeMessage> findCompanyNoticeList(Integer type, Integer offset, Integer limit) {
        if (AgentCompanyNoticeMessage.Type.NOTICE.getValue() == type) {
            return publicNoticeMapper.findCompanyNoticeList(AgentCompanyPublicNotice.NoticeType.AGENT_COMPANY_NOTICE.getValue(), offset, limit);
        } else {
            return null;
        }
    }

    public AgentCompanyNoticeMessage findCompanyNotice(Integer type, Long id) {
        if (AgentCompanyNoticeMessage.Type.NOTICE.getValue() == type) {
            return publicNoticeMapper.findCompanyNotice(PublicNotice.NoticeType.AGENT_COMPANY_NOTICE.getValue(), id);
        } else {
            return null;
        }
    }

}
