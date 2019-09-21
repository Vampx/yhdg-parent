package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface InsuranceOrderMapper extends MasterMapper {
    InsuranceOrder find(@Param("id")String id);

    List<InsuranceOrder> findListByCustomerId(@Param("customerId") long customerId, @Param("agentId") Integer agentId);

    List<InsuranceOrder> findCanRefundByCustomerId(Long customerId);

    int findPageCount(InsuranceOrder insuranceOrder);

    List<InsuranceOrder> findPageResult(InsuranceOrder entity);

    int updateStatus(@Param("customerId")long customerId, @Param("status") int status);

    int updatePayTime(@Param("id") String id, @Param("payTime") Date payTime);

    int updateRefund(@Param("id") String id,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("fromStatus") int fromStatus,
                     @Param("toStatus") int toStatus
    );

}
