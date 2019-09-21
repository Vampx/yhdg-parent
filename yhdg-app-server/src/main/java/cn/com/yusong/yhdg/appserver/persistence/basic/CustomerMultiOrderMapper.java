package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMultiOrderMapper extends MasterMapper {
    int insert(CustomerMultiOrder customerMultiOrder);

    CustomerMultiOrder find(long id);

    int updateDebtMoneyAndStatus(@Param("id") Long id, @Param("debtMoney") int debtMoney, @Param("status") int status);

    List<CustomerMultiOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    int countMultiWaitPay(@Param("customerId") Long customerId, @Param("type") int type, @Param("statusList") List statusList);

    int updateRefund(@Param("id") long id, @Param("toStatus") int toStatus, @Param("fromStatus") Integer fromStatus);
}
