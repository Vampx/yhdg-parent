package cn.com.yusong.yhdg.agentappserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.AgentSystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentSystemConfigMapper extends MasterMapper {
    public AgentSystemConfig find(@Param("id") String id, @Param("agentId") Integer agentId);
    public String findConfigValue(@Param("id") String id,@Param("agentId") Integer agentId);
    public int insert(@Param("sql") String sql);
    public void update(AgentSystemConfig systemConfig);
    public int deleteByAgentId(Integer agentId);

}

