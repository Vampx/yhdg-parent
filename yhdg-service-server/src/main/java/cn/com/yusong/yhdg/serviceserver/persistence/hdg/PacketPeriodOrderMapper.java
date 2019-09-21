package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderMapper extends MasterMapper {
    public PacketPeriodOrder find(@Param("id") String id);
	
    public String findMaxId();

    public List<PacketPeriodOrder> findExpireList(@Param("expireTime") Date expireTime, @Param("offset") int offset, @Param("limit") int limit);

    public List<PacketPeriodOrder> findAllByCursor(@Param("id") String id, @Param("limit") int limit);

    public List<PacketPeriodOrder> findByCursorNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("id") String id, @Param("limit") int limit);

    public List<CabinetDayStats> findCabinetIncrement( @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<PacketPeriodOrder> findIncrementExchange( @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<PacketPeriodOrder> findRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<AgentDayStats> findAgentIncrement(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<String> findExpiredOrder(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    PacketPeriodOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("batteryType") int batteryType);

    int updateStatus(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int updateUsedOrder(@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("beginTime") Date beginTime, @Param("limit") int limit);

    public int updateExpiredOrder(@Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus, @Param("expireTime") Date expireTime, @Param("limit") int limit);

    public int updateExpireNoticeTime(@Param("id") String id, @Param("expireNoticeTime") Date expireNoticeTime);

}
