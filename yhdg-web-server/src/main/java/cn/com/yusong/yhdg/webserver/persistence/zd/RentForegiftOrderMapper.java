package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentForegiftOrderMapper extends MasterMapper {
    RentForegiftOrder find(String id);

    int sumMoneyByAgent( @Param("agentId") int agentId, @Param("status") List<Integer> status);

    List<RentForegiftOrder> findByForegiftIdAndStatus(@Param("foregiftId") long foregiftId, @Param("foregiftList") List<Integer> foregiftList);

    int findPageCount(RentForegiftOrder customerForegiftOrder);

    List<RentForegiftOrder> findPageResult(RentForegiftOrder customerForegiftOrder);

    List<RentForegiftOrder> findListByCustomerId(@Param("customerId") long customerId, @Param("agentId") Integer agentId);

    List<RentForegiftOrder> findCanRefundByCustomerId(Long customerId);

    RentForegiftOrder findByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") Integer status);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateStatus(@Param("id") String id, @Param("status") int status,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime,
                     @Param("refundPhoto") String refundPhoto,
                     @Param("memo") String memo,
                     @Param("handleTime") Date handleTime);

    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int updateRefund(@Param("id")String id,
                     @Param("applyRefundTime")Date applyRefundTime,
                     @Param("memo")String memo,
                     @Param("toStatus")int toStatus,
                     @Param("fromStatus")int fromStatus);

}
