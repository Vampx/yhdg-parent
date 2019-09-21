package cn.com.yusong.yhdg.weixinserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface LaxinMapper extends MasterMapper {
    public Laxin find(long id);
    public Laxin findByAgentMobile(@Param("agentId") int agentId, @Param("mobile")String mobile);
}
