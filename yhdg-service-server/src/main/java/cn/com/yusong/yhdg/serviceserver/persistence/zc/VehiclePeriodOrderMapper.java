package cn.com.yusong.yhdg.serviceserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehiclePeriodOrderMapper extends MasterMapper {

    public List<VehiclePeriodOrder> findListByStatus(@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    public VehiclePeriodOrder findUsedByCustomer(@Param("customerId") Long customerId, @Param("status") Integer status);

    public int updateUsedOrder(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int updateExpiredOrder(@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("expireTime") Date expireTime, @Param("limit") int limit);

}
