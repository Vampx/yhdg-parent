package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderRefundMapper extends MasterMapper {
    public List<PacketPeriodOrder> findAgentIncrement(@Param("refundStatus") Integer refundStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
