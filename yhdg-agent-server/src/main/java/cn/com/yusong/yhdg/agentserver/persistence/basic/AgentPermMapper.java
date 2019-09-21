package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentPerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentPermMapper extends MasterMapper {
    public List<AgentPerm> findAllByClientType(@Param("clientType") Integer clientType);
    public List<AgentPerm> findAll();
    public List<AgentPerm> findAllAppPerm();
    public AgentPerm find(@Param("id") String id);
    public AgentPerm findAppPerm(@Param("id") String id);
}
