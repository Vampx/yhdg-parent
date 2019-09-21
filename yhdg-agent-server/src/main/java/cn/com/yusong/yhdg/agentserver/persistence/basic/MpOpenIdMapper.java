package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface MpOpenIdMapper extends MasterMapper {
    public PartnerMpOpenId find(@Param("openId") String openId);
    public int updateCustomerId(@Param("openId") String openId, @Param("customerId") Long customerId);
    public PartnerMpOpenId fingCustomerId(@Param("customerId") long customerId);
}
