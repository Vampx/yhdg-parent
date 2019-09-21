package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentNoticeMessageMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentPublicNoticeMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.PublicNoticeMapper;
import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.common.domain.basic.PublicNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentNoticeMessageService {
	@Autowired
	AgentNoticeMessageMapper agentNoticeMessageMapper;
	@Autowired
	AgentPublicNoticeMapper agentPublicNoticeMapper;
	@Autowired
	PublicNoticeMapper publicNoticeMapper;

	public AgentNoticeMessage find(Integer type, Long id) {
		if (AgentNoticeMessage.Type.NOTICE.getValue() == type) {
			return publicNoticeMapper.find(PublicNotice.NoticeType.AGENT_NOTICE.getValue(), id);
//			return agentPublicNoticeMapper.find(AgentPublicNotice.NoticeType.AGENT_NOTICE.getValue(), id);
		}
		return agentNoticeMessageMapper.find(id, type);
	}

	public List<AgentNoticeMessage> findListByCustomerId(int agentId, Integer type, Integer offset, Integer limit) {
		if (AgentNoticeMessage.Type.NOTICE.getValue() == type) {
			return publicNoticeMapper.findList(PublicNotice.NoticeType.AGENT_NOTICE.getValue(), offset, limit);
//			return agentPublicNoticeMapper.findList(AgentPublicNotice.NoticeType.AGENT_NOTICE.getValue(), agentId, offset, limit);
		}
		return agentNoticeMessageMapper.findListByCustomerId(agentId, type, offset, limit);
	}
}
