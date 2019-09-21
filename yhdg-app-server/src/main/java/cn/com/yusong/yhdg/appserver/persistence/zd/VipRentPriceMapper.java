package cn.com.yusong.yhdg.appserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface VipRentPriceMapper extends MasterMapper {
    VipRentPrice findOneByShopId(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("shopId") String shopId, @Param("now") Date now);
    VipRentPrice findOneByCustomerMobile(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("mobile") String mobile, @Param("now") Date now);
    VipRentPrice findOneByAgentCompanyId(@Param("agentId") int agentId, @Param("batteryType") int batteryType, @Param("agentCompanyId") String agentCompanyId, @Param("now") Date now);
}
