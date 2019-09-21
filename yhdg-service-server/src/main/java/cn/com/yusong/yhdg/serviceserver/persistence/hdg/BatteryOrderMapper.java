package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BatteryOrderMapper extends MasterMapper {
    public BatteryOrder find(@Param("id") String id);

    public String findMaxId();

    public List<BatteryOrder> findAllByCursor(@Param("id") String id, @Param("limit") int limit);

    public BatteryOrder findLastByBattery(@Param("batteryId") String batteryId);

    public BatteryOrder findLastByCustomer(@Param("batteryId") String batteryId, @Param("putTime") Date putTime, @Param("takeTime") Date takeTime,  @Param("customerId") Long customerId, @Param("orderStatus") Integer orderStatus);

    public List<BatteryOrder> findByCursorAndAgent(@Param("id") String id, @Param("agentId") int agentId, @Param("limit") int limit);

    public List<BatteryOrder> findByCursorNotAgent(@Param("id") String id, @Param("agentIdList") List<Integer> agentIdList, @Param("limit") int limit);

    public List<BatteryOrder> findAllForPut(@Param("putTime") Date putTime, @Param("limit") int limit);

    public List<BatteryOrder> findAllForPutByAgent(@Param("agentId") int agentId, @Param("putTime") Date putTime, @Param("limit") int limit);

    public List<BatteryOrder> findAllForPutNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("putTime") Date putTime, @Param("limit") int limit);

    public List<String> findHistoryId(@Param("dayId") String dayId, @Param("orderStatus") Integer orderStatus);

    public List<CabinetDayStats> findCabinetExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<BatteryOrder> findIncrementExchange(@Param("orderStatus") Integer orderStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<BatteryOrder> findRefund(@Param("refundStatus") Integer refundStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payTypeList") List<Integer> payTypeList);

    public List<CabinetDayStats> findActiveCustomerCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<AgentDayStats> findAgentActiveCustomerCount(@Param("orderStatus") Integer orderStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<CustomerDayStats> findIncrementByCustomer(@Param("orderStatus") Integer orderStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<BatteryOrder> findPayTimeout(@Param("orderStatus") Integer orderStatus, @Param("putTime") Date putTime);

    public List<BatteryOrder> findNotTakeTimeout(@Param("orderStatus") Integer orderStatus, @Param("createTime") Date createTime);

    public List<BatteryOrder> findTakeTimeout(@Param("orderStatus") Integer orderStatus, @Param("createTime") Date createTime);

    public List<Map> findIncrementPacketPeriodCount(@Param("orderStatus") Integer orderStatus, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("payType") Integer payType);

    public List<CabinetDayStats> findIncrementPacketPeriod(@Param("packetPeriodOrderId") String packetPeriodOrderId);

    int findTotalCount( @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int findTotalCountByNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int findTotalCountByAgent(@Param("agentId") int agentId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<BatteryOrder> findCountByCity(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<BatteryOrder> findCountByCityAndAgent(@Param("agentId") int agentId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<BatteryOrder> findCountByCityAndNotAgent(@Param("agentIdList") List<Integer> agentIdList, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public int insert(BatteryOrder batteryOrder);

    public int updatePayTimeout(@Param("id") String id, @Param("payTimeoutFaultLogId") Long payTimeoutFaultLogId);

    public int updateNotTakeTimeout(@Param("id") String id, @Param("notTakeTimeoutFaultLogId") Long notTakeTimeoutFaultLogId);

    public int updateTakeTimeout(@Param("id") String id, @Param("takeTimeoutFaultLogId") Long takeTimeoutFaultLogId);

    int updateLowVolumeVoiceTime(@Param("id") String id, @Param("lowVolumeVoiceTime") Date lowVolumeVoiceTime);

    public int payOk(@Param("id") String id, @Param("fromStatus") Integer fromStatus, @Param("toStatus") int toStatus,
                     @Param("putCabinetId") String putCabinetId,  @Param("putCabinetName") String putCabinetName,  @Param("putBoxNum") String putBoxNum,  @Param("putTime") Date putTime,
                     @Param("payType") int payType, @Param("payTime") Date payTime, @Param("price") int price, @Param("money") int money,
                     @Param("ticketMoney") Integer ticketMoney, @Param("ticketName") String ticketName, @Param("couponTicketId") Long couponTicketId);


    public int deleteHistory(@Param("monthId") String monthId, @Param("dayId") String dayId, @Param("orderStatus") Integer orderStatus);
}