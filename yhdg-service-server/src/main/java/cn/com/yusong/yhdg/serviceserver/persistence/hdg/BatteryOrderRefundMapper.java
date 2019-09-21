package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BatteryOrderRefundMapper extends MasterMapper {
    public List<BatteryOrder> findIncrement(@Param("refundStatus") Integer refundStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}
