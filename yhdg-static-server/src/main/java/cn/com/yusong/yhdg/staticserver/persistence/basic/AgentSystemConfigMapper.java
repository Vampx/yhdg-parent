package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentSystemConfigMapper extends MasterMapper {

    public String findConfigValue(@Param("agentId") int agentId, @Param("id") String id);
}
