package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentFwOpenIdMapper extends MasterMapper {
    public AlipayfwOpenId find(@Param("agentId") int agentId, @Param("openId") String openId);
}
