package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface InsuranceOrderMapper extends MasterMapper {

    public List<InsuranceOrder> findIncrement(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                                     @Param("limit") int limit);

    public List<InsuranceOrder> findRefund(@Param("agentId") Integer agentId, @Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("offset") int offset,
                                                  @Param("limit") int limit);

    public InsuranceOrder findByCustomerId(@Param("customerId")long customerId,   @Param("batteryType") Integer batteryType, @Param("status") Integer status);

}
