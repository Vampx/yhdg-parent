package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentPeriodOrderMapper extends MasterMapper {
    List<RentPeriodOrder> findListByCustomerId(@Param("customerId") long customerId);

    List<RentPeriodOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    List<RentPeriodOrder> findList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit);

    List<RentPeriodOrder> findNoStatusList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") int status);

    RentPeriodOrder findLastEndTime(@Param("customerId") long customerId);

    RentPeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    RentPeriodOrder findRemainingTime(@Param("customerId") long customerId, @Param("status") Integer status);

    RentPeriodOrder find(String id);

    List<RentPeriodOrder> findNeedRefundList(@Param("customerId") long customerId, @Param("beginTime") Date beginTime, @Param("status") int status);

    int insert(RentPeriodOrder packetPeriodOrder);

    int updateStatus(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateRefund(@Param("id") String id,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);

    int updateOrderStatus(@Param("id") String id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
