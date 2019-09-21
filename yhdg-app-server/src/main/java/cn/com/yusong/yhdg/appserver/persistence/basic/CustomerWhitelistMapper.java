package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CustomerWhitelistMapper extends MasterMapper {
    int findByMobile(@Param("partnerId") Integer partnerId,  @Param("agentId") Integer agentId, @Param("mobile") String mobile);
}
