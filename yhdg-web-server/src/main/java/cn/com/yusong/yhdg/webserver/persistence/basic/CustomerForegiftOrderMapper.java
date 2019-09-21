package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerForegiftOrderMapper extends MasterMapper {
    CustomerForegiftOrder find(String id);

    int sumMoneyByAgent( @Param("agentId") int agentId, @Param("status") List<Integer> status);

    List<CustomerForegiftOrder> findByForegiftIdAndStatus(@Param("foregiftId") Long foregiftId, @Param("foregiftList") List<Integer> foregiftList);

    int findPageCount(CustomerForegiftOrder customerForegiftOrder);

    List<CustomerForegiftOrder> findPageResult(CustomerForegiftOrder customerForegiftOrder);

    List<CustomerForegiftOrder> findListByCustomerId(@Param("customerId") long customerId, @Param("agentId") Integer agentId);

    CustomerForegiftOrder findByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") Integer status);

    List<CustomerForegiftOrder> findCanRefundByCustomerId(Long customerId);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateStatus(@Param("id") String id, @Param("status") int status,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime,
                     @Param("refundPhoto") String refundPhoto,
                     @Param("memo") String memo,
                     @Param("handleTime") Date handleTime);

    int updateOrder(@Param("id") String id,
                    @Param("customerId") long customerId,
                    @Param("customerMobile") String customerMobile,
                    @Param("customerFullname") String customerFullname,
                    @Param("payType") Integer payType,
                    @Param("createTime") Date createTime);


    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int updateRefund(@Param("id")String id,
                     @Param("applyRefundTime")Date applyRefundTime,
                     @Param("memo")String memo,
                     @Param("toStatus")int toStatus,
                     @Param("fromStatus")int fromStatus);

}
