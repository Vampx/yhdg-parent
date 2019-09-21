package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface InsuranceOrderMapper extends MasterMapper {
    InsuranceOrder find(String id);

    InsuranceOrder findByCustomerId(@Param("customerId") long customerId, @Param("batteryType") Integer batteryType, @Param("status") Integer status);

    List<InsuranceOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    List<InsuranceOrder> findList(@Param("agentId") Integer agentId, @Param("customerId") long customerId, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    InsuranceOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    int insert(InsuranceOrder insuranceOrder);

    int updateRefund(@Param("id") String id, @Param("toStatus") int toStatus, @Param("fromStatus") Integer fromStatus);

    int updateOrderStatus(@Param("id") String id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

}
