package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface PacketPeriodOrderMapper extends MasterMapper {
    List<PacketPeriodOrder> findListByCustomerId(@Param("customerId") long customerId);

    List<PacketPeriodOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    List<PacketPeriodOrder> findList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit);

    List<PacketPeriodOrder> findExpireList(@Param("expireTime") Date expireTime, @Param("agentId") int agentId);

    List<PacketPeriodOrder> findNoStatusList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") int status);

    PacketPeriodOrder findLastEndTime(@Param("customerId") long customerId, @Param("status") Integer status);

    List<PacketPeriodOrder>  findListByNoUsed(@Param("customerId") long customerId, @Param("status") Integer status);

    PacketPeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    PacketPeriodOrder findOneEnabledByAgent(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") Integer agentId);

    PacketPeriodOrder findRemainingTime(@Param("customerId") long customerId, @Param("status") Integer status);

    PacketPeriodOrder find(String id);

    List<PacketPeriodOrder> findNeedRefundList(@Param("customerId") long customerId, @Param("beginTime") Date beginTime, @Param("status") int status);

    int insert(PacketPeriodOrder packetPeriodOrder);

    int updateStatus(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateRefund(@Param("id") String id,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);

    int updateOrderStatus(@Param("id") String id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateOrderCount(@Param("id") String id);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime);

}
