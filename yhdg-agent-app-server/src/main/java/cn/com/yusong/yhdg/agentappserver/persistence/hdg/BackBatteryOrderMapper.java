package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BackBatteryOrderMapper extends MasterMapper {
    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findPageCount(BackBatteryOrder batteryOrder);

    List findPageResult(BackBatteryOrder batteryOrder);

    BackBatteryOrder find(String id);

    BackBatteryOrder findByCustomerId(@Param("customerId") long customerId, @Param("orderStatus") Integer orderStatus);

    int existLastOrder(@Param("agentId") Integer agentId, @Param("customerId") long customerId, @Param("orderStatus") Integer orderStatus);

    int updateStatus(@Param("id") String id, @Param("orderStatus") Integer orderStatus, @Param("cancelTime") Date cancelTime);

    int deleteByCustomerId(@Param("customerId") long customerId);

    void insert(BackBatteryOrder backBatteryOrder);
}
