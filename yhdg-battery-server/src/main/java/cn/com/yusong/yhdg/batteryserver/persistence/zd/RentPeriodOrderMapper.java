package cn.com.yusong.yhdg.batteryserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/6.
 */
public interface RentPeriodOrderMapper extends MasterMapper {
    public List<RentPeriodOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    public RentPeriodOrder findLastEndTime(@Param("customerId") long customerId);

}
