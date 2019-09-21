package cn.com.yusong.yhdg.serviceserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentPeriodOrderMapper extends MasterMapper {
    public RentPeriodOrder find(@Param("id") String id);

    public List<RentPeriodOrder> findListByStatus(@Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    public RentPeriodOrder findUsedByCustomer(@Param("customerId") Long customerId, @Param("status") Integer status);

    public List<RentPeriodOrder> findIncrementExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<RentPeriodOrder> findRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public int updateUsedOrder(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int updateExpiredOrder(@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("expireTime") Date expireTime, @Param("limit") int limit);

}
