package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentPermMapper extends MasterMapper {
    public List<String> findAllByClientType(@Param("clientType") Integer clientType);
}
