package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderMapper extends MasterMapper {
    public PacketPeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    public PacketPeriodOrder findOneEnabledByAgent(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") Integer agentId);

    public int updateStatus(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int updateOrderCount(@Param("id") String id);
}