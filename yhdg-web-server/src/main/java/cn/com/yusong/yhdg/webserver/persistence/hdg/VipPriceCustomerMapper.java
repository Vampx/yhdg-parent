package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceCustomerMapper extends MasterMapper {
    List<VipPriceCustomer> findListByPriceId(@Param("priceId") long priceId);
    List<VipPriceCustomer> findListByMobile(@Param("mobile") String mobile);
    VipPriceCustomer findByAgentIdAndMobile(@Param("agentId") Integer agentId, @Param("mobile") String mobile);
    int insert(VipPriceCustomer vipPriceCustomer);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
