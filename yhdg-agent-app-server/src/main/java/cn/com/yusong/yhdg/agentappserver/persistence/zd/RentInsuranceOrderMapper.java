package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentInsuranceOrderMapper extends MasterMapper {
    List<RentInsuranceOrder> findIncrement(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                              @Param("limit") int limit);
    List<RentInsuranceOrder> findRefund(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                           @Param("limit") int limit);
    RentInsuranceOrder findByCustomerId(@Param("customerId")long customerId, @Param("batteryType") Integer batteryType, @Param("status") Integer status);
}
