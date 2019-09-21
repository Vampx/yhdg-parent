package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyNoticeMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentCompanyNoticeMessageMapper extends MasterMapper {
    AgentCompanyNoticeMessage find(@Param("id") long id, @Param("type") Integer type);

    List<AgentCompanyNoticeMessage> findListByCustomerId(@Param("agentId") int agentId, @Param("agentCompanyId") String agentCompanyId, @Param("type") Integer type, @Param("offset") Integer offset, @Param("limit") Integer limit);

}
