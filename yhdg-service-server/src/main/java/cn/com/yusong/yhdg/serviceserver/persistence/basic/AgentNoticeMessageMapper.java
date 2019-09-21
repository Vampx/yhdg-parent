package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;


public interface AgentNoticeMessageMapper extends MasterMapper {
    public int insert(AgentNoticeMessage noticeMessage);
}
