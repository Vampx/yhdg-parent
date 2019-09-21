package cn.com.yusong.yhdg.staticserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface VehiclePeriodOrderMapper extends MasterMapper {
    VehiclePeriodOrder find(String id);
    VehiclePeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("modelId") int modelId);
    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
