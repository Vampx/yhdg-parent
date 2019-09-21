package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentNoticeMessageMapper extends MasterMapper {
    AgentNoticeMessage find(@Param("id") long id, @Param("type") Integer type);

    List<AgentNoticeMessage> findListByCustomerId(@Param("agentId") int agentId, @Param("type") Integer type, @Param("offset") Integer offset, @Param("limit") Integer limit);

}
