package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerRefundRecordMapper extends MasterMapper {
    CustomerRefundRecord find(@Param("id") long id);

    int existHdRefund(@Param("customerId") long customerId);

    int existZdRefund(@Param("customerId") long customerId);

    int existZcRefund(@Param("customerId") long customerId);

    int findPageCount(CustomerRefundRecord customer);

    List<CustomerRefundRecord> findPageResult(CustomerRefundRecord customer);

    List<CustomerRefundRecord> findByCustomerId(@Param("customerId") Long customerId, @Param("status") int status);

    CustomerRefundRecord findOneByStatus(@Param("customerId") Long customerId, @Param("status") Integer status, @Param("sourceType") Integer sourceType);

    CustomerRefundRecord findOneByCustomerId(@Param("customerId") Long customerId, @Param("status") int status);

    int insert(CustomerRefundRecord refundRecord);

    int updateStatus(@Param("id") Long id,
                     @Param("refundType") int refundType,
                     @Param("ptPayOrderId") String ptPayOrderId,
                     @Param("status") int status,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundTime") Date refundTime);

    int backStatus(@Param("id") Long id,
                   @Param("status") int status,
                   @Param("refundMoney") int refundMoney,
                   @Param("refundTime") Date refundTime);
}
