package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentMpOpenIdMapper extends MasterMapper {
    public WeixinmpOpenId find(@Param("agentId") int agentId, @Param("openId") String openId);
}
