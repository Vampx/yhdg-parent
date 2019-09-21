package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FaultLogMapper extends MasterMapper {
    public int findMaxId();

    public List<FaultLog> findList(@Param("batteryId") String batteryId, @Param("faultType") int faultType);

    public List<FaultLog> findAllByCursor(@Param("id") int id, @Param("limit") int limit);

    public List<FaultLog> findByCursorNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("id") int id, @Param("limit") int limit);

    public List<FaultLog> findAllByCursorAndAgent(@Param("agentId") int agentId, @Param("id") int id, @Param("limit") int limit);

    public int findTotalCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int findTotalCountByAgent(@Param("agentId") int agentId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int findTotalCountByAgentAndType(@Param("agentId") int agentId, @Param("faultType") int faultType);

    public int findTotalCountByAgentAndTypeAndBattery(@Param("agentId") int agentId, @Param("faultType") int faultType, @Param("batteryId") String batteryId, @Param("cabinetId") String cabinetId,  @Param("boxNum") String boxNum);

    public int findTotalCountByNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int findTotalCountAndAgent(@Param("agentId") int agentId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int create(FaultLog faultLog);

    public int handle(@Param("id") long id,
               @Param("handleType") int handleType,
               @Param("handleMemo") String handleMemo,
               @Param("handleTime") Date handleTime,
               @Param("handlerName") String handlerName,
               @Param("status") int status
    );
}
