package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryOrderHistoryMapper extends HistoryMapper {
    List<String> findTable(@Param("tableName") String tableName);

    int findPageCount(BatteryOrder batteryOrder);

    List<BatteryOrder> findPageResult(BatteryOrder batteryOrder);

    BatteryOrder find(@Param("id") String id, @Param("suffix") String suffix);

}