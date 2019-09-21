package cn.com.yusong.yhdg.staticserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface RentPeriodOrderMapper extends MasterMapper {
    RentPeriodOrder find(String id);

    RentPeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    int payOk(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("payTime") Date payTime, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updatePaidMoney(@Param("id") String id, @Param("recordId") long recordId, @Param("status") int status);

    int updateCompleteInstallmentTime(@Param("id") String id,
                                      @Param("completeInstallmentTime") Date completeInstallmentTime,
                                      @Param("payTime") Date payTime);

}
