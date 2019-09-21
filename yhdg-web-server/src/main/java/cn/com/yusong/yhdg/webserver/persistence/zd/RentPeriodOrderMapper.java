package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentPeriodOrderMapper extends MasterMapper {
    int findCountByAgentCompany(@Param("agentCompanyId") String agentCompanyId);

    int findPageCount(RentPeriodOrder rentPeriodOrder);

    List<RentPeriodOrder> findListByCustomerId(@Param("customerId") Long customerId, @Param("agentId") Integer agentId);

    List<RentPeriodOrder> findPageResult(RentPeriodOrder rentPeriodOrder);

    int findPageForBalanceCount(RentPeriodOrder rentPeriodOrder);

    List<RentPeriodOrder> findList(long customerId);

    List<RentPeriodOrder> findPageForBalanceResult(RentPeriodOrder rentPeriodOrder);

    RentPeriodOrder findOneEnabledLast(@Param("customerId") Long customerId, @Param("status") List<Integer> status);

    List<RentPeriodOrder> findCanRefundByCustomerId(Long customerId);

    int updateRefund(@Param("id") String id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("toStatus") int toStatus);

    int backRefund(@Param("id") String id,
                   @Param("fromStatus") int fromStatus,
                   @Param("toStatus") int toStatus);

    RentPeriodOrder find(String id);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateStatus(@Param("customerId") long customerId, @Param("status") int status);

    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int extendRent(@Param("id") String id,
                   @Param("dayCount") Integer dayCount,
                   @Param("endTime") Date endTime,
                   @Param("status") Integer status,
                   @Param("operatorMemo") String operatorMemo);
}
