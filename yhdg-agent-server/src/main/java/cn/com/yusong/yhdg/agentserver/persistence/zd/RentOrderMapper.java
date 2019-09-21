package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentOrderMapper extends MasterMapper {
    int findPageCount(RentOrder rentOrder);

    List<RentOrder> findListByCustomerId(@Param("customerId") Long customerId, @Param("agentId") Integer agentId);

    List<RentOrder> findPageResult(RentOrder rentOrder);

    int findPageForBalanceCount(RentOrder rentOrder);

    List<RentOrder> findList(long customerId);

    List<RentOrder> findPageForBalanceResult(RentOrder rentOrder);

    List<RentOrder> findCanRefundByCustomerId(Long customerId);

    int updateRefund(@Param("id") String id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("toStatus") int toStatus);



    RentOrder find(String id);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateStatus(@Param("customerId") long customerId, @Param("status") int status);
    int updateStatusById(String id);
    int updateBattery(@Param("id") String id, @Param("batteryId") String batteryId);

    int complete(@Param("id") String id,
                 @Param("toStatus") int toStatus,
                 @Param("fromStatus") int fromStatus,
                 @Param("backTime") Date backTime,
                 @Param("backOperator")String backOperator);
}

