package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentInsuranceOrderMapper extends MasterMapper {
    RentInsuranceOrder find(@Param("id")String id);

    List<RentInsuranceOrder> findListByCustomerId(@Param("customerId") long customerId, @Param("agentId") Integer agentId);

    List<RentInsuranceOrder> findCanRefundByCustomerId(Long customerId);

    int findPageCount(RentInsuranceOrder insuranceOrder);

    List<RentInsuranceOrder> findPageResult(RentInsuranceOrder entity);

    int updateStatus(@Param("customerId")long customerId, @Param("status") int status);

    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int updateRefund(@Param("id") String id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("fromStatus") int fromStatus,
                     @Param("toStatus") int toStatus
    );

}
