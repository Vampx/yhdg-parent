package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerFwOpenId;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface FwOpenIdMapper extends MasterMapper {
    public PartnerFwOpenId find(@Param("openId") String openId);
    public int updateCustomerId(@Param("openId") String openId, @Param("customerId") Long customerId);
}
