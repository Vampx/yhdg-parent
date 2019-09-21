package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderMapper extends MasterMapper {
    int findCountByAgentCompany(@Param("agentCompanyId") String agentCompanyId);

    int findPageCount(PacketPeriodOrder packetPeriodOrder);

    List<PacketPeriodOrder> findListByCustomerId(@Param("customerId") Long customerId, @Param("agentId") Integer agentId);

    List<PacketPeriodOrder> findPageResult(PacketPeriodOrder packetPeriodOrder);

    int findPageForBalanceCount(PacketPeriodOrder packetPeriodOrder);

    List<PacketPeriodOrder> findList(long customerId);

    List<PacketPeriodOrder> findPageForBalanceResult(PacketPeriodOrder packetPeriodOrder);

    PacketPeriodOrder findOneEnabledLast(@Param("customerId") Long customerId, @Param("status") List<Integer> status);

    List<PacketPeriodOrder> findListByCustomerIdAndStatus(@Param("customerId")Long customerId, @Param("status") List<Integer> status);

    List<PacketPeriodOrder> findCanRefundByCustomerId(Long customerId);

    int findPageForClearingCount(PacketPeriodOrder packetPeriodOrder);

    List<PacketPeriodOrder> findPageForClearingResult(PacketPeriodOrder packetPeriodOrder);

    List<PacketPeriodOrder> findBySoonExpirePageResult(PacketPeriodOrder packetPeriodOrder);

    List<PacketPeriodOrder> findBySoonExpireAll(PacketPeriodOrder packetPeriodOrder);

    int findBySoonExpirePageCount(PacketPeriodOrder packetPeriodOrder);

    int updateRefund(@Param("id") String id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("toStatus") int toStatus);

    int updateOrder(@Param("id") String id,
                    @Param("customerId") long customerId,
                    @Param("customerMobile") String customerMobile,
                    @Param("customerFullname") String customerFullname,
                    @Param("payType") Integer payType,
                    @Param("createTime") Date createTime);

    int backRefund(@Param("id") String id,
                            @Param("fromStatus") int fromStatus,
                            @Param("toStatus") int toStatus
    );

    PacketPeriodOrder find(String id);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateStatus(@Param("customerId")long customerId, @Param("status") int status);

    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int extendRent(@Param("id") String id, @Param("dayCount") Integer dayCount, @Param("endTime") Date endTime,
                   @Param("status") Integer status, @Param("operatorMemo") String operatorMemo);

}
