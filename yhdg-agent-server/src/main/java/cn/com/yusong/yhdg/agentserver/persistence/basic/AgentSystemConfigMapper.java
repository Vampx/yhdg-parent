package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentSystemConfigMapper extends MasterMapper {
    public List<AgentSystemConfig> findAllCategoryByAgentId(Integer agentId);
    public List<AgentSystemConfig> findByAgentId(Integer agentId);
    public AgentSystemConfig find(@Param("id")String id, @Param("agentId")Integer agentId);
    public List<AgentSystemConfig> findAll(AgentSystemConfig systemConfig);
    public String findConfigValue(@Param("id") String id,@Param("agentId") Integer agentId);
    public int insert(@Param("sql")String sql);
    public int insertOne(AgentSystemConfig agentSystemConfig);
    public void update(AgentSystemConfig systemConfig);
    public int deleteByAgentId(Integer agentId);

}

