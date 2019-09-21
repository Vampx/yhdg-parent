package cn.com.yusong.yhdg.appserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentInsuranceOrderMapper extends MasterMapper {
    RentInsuranceOrder find(String id);

    RentInsuranceOrder findByCustomerId(@Param("customerId") long customerId, @Param("batteryType") Integer batteryType, @Param("status") Integer status);

    List<RentInsuranceOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    List<RentInsuranceOrder> findList(@Param("agentId") Integer agentId, @Param("customerId") long customerId, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    RentInsuranceOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    int insert(RentInsuranceOrder rentInsuranceOrder);

    int updateRefund(@Param("id") String id, @Param("toStatus") int toStatus, @Param("fromStatus") Integer fromStatus);

    int updateOrderStatus(@Param("id") String id,
                          @Param("toStatus") int toStatus,
                          @Param("fromStatus") Integer fromStatus);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime);

}
