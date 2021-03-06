package cn.com.yusong.yhdg.webserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPriceCustomerMapper extends MasterMapper {
    List<VipRentPriceCustomer> findListByPriceId(@Param("priceId") long priceId);
    VipRentPriceCustomer findByAgentIdAndMobile(@Param("agentId") Integer agentId, @Param("mobile") String mobile);
    int insert(VipRentPriceCustomer vipRentPriceCustomer);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
