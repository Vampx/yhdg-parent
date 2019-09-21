package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerRefundRecordMapper extends MasterMapper {

    int insert(CustomerRefundRecord refundRecord);

    List<CustomerRefundRecord> findListByCustomerIdAndStatus(@Param("customerId") long customerId,
                                                             @Param("status") Integer status,
                                                             @Param("offset") Integer offset,
                                                             @Param("limit") Integer limit,
                                                             @Param("type") int type);

    List<CustomerRefundRecord> findListByorderId(@Param("customerId") long customerId, @Param("orderId") String orderId);

    int updateRefund(@Param("id") long id,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus,
                     @Param("cancelTime") Date cancelTime);
}