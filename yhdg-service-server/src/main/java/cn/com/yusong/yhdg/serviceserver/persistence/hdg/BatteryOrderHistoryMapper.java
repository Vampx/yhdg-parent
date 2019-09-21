package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryOrderHistoryMapper extends HistoryMapper {
    int createTable(@Param("suffix") String suffix);

    List<String> findTable(@Param("tableName") String tableName);

    int findTotalCount(@Param("searchTableName") String searchTableName, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int findTotalCountByNotAgent(@Param("agentIdList")List<Integer> agentIdList, @Param("searchTableName") String searchTableName, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int findTotalCountByAgent(@Param("agentId") int agentId, @Param("searchTableName") String searchTableName, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<BatteryOrder> findCountByCity(@Param("searchTableName") String searchTableName);

    public List<BatteryOrder> findCountByCityAndAgent(@Param("agentId") int agentId, @Param("searchTableName") String searchTableName);

    public List<BatteryOrder> findCountByCityAndNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("searchTableName") String searchTableName);

    int move(@Param("suffix") String suffix, @Param("monthId") String monthId, @Param("dayId") String dayId, @Param("orderStatus") Integer orderStatus);
}