package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface CustomerForegiftOrderMapper extends MasterMapper {

    CustomerForegiftOrder find(String id);
    int findCountByCustomerId(@Param("id") String id, @Param("agentId") int agentId, @Param("customerId") long customerId, @Param("status") int status);
    int sumMoneyByAgent(@Param("agentId") int agentId, @Param("status") List<Integer> status);
    List<CustomerForegiftOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId,@Param("status")int status);
    int insert(CustomerForegiftOrder customerForegiftOrder);
    int refund(@Param("id") String id,
               @Param("applyRefundTime") Date applyRefundTime,
               @Param("refundReason") String refundReason,
               @Param("refundTime") Date refundTime,
               @Param("refundMoney") int refundMoney,
               @Param("toStatus") int toStatus,
               @Param("fromStatus") int fromStatus);
    int updateRefund(@Param("id") String id,
                     @Param("applyRefundTime") Date applyRefundTime,
                     @Param("refundReason") String refundReason,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);
    int updateOrderStatus(@Param("id") String id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

    int payOk(@Param("id") String id, @Param("handleTime") Date handleTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int payMultiOk(@Param("id") String id, @Param("handleTime") Date handleTime, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

}
