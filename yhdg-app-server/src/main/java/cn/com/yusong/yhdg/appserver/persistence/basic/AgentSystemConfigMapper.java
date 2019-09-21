package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentSystemConfigMapper extends MasterMapper {

    public String findConfigValue(@Param("id") String id,@Param("agentId") Integer agentId);

}
