package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerFwOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface PartnerFwOpenIdMapper extends MasterMapper {
    public PartnerFwOpenId findByOpenId(@Param("partnerId")int partnerId, @Param("openId") String openId);
    public int updateCustomerId(@Param("partnerId")int partnerId, @Param("openId") String openId, @Param("customerId") Long customerId);
}
