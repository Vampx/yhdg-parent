package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentPublicNotice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentPublicNoticeMapper extends MasterMapper {
    AgentPublicNotice find(long id);
    int findPageCount(AgentPublicNotice search);
    List<AgentPublicNotice> findPageResult(AgentPublicNotice search);
    int insert(AgentPublicNotice entity);
    int update(AgentPublicNotice entity);
    int delete(long id);
}
