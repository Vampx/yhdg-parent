package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface VehiclePeriodOrderMapper extends MasterMapper {
    VehiclePeriodOrder find(String id);

    List<VehiclePeriodOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    VehiclePeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("modelId") int modelId);

    List<VehiclePeriodOrder> findList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") Integer status);

    int insert(VehiclePeriodOrder vehiclePeriodOrder);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateStatus(@Param("id") String id,
                     @Param("fromStatus") Integer fromStatus,
                     @Param("toStatus") Integer toStatus,
                     @Param("beginTime") Date beginTime,
                     @Param("endTime") Date endTime,
                     @Param("vehicleName") String vehicleName);

    int updateRefund(@Param("id") String id,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);

    VehiclePeriodOrder findLastEndTime(@Param("customerId") long customerId, @Param("status") int status);

}
