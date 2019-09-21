package cn.com.yusong.yhdg.agentserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPriceCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VehicleVipPriceCustomerMapper extends MasterMapper {
    List<VehicleVipPriceCustomer> findListByPriceId(@Param("priceId") long priceId);
    VehicleVipPriceCustomer findByAgentIdAndMobile(@Param("agentId") Integer agentId, @Param("mobile") String mobile);
    int insert(VehicleVipPriceCustomer vehicleVipPriceCustomer);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
