package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryOrderMapper extends MasterMapper {

    BatteryOrder find(String id);

    BatteryOrder findByBatteryId(String batteryId);

    List<BatteryOrder> findListByCustomer(@Param("customerId") long customerId,
                                          @Param("orderStatus") Integer orderStatus);

    List<BatteryOrder> findListByCabinetId(@Param("agentId") Integer agentId,
                                           @Param("cabinetId") String cabinetId,
                                           @Param("beginTime") Date beginTime,
                                           @Param("endTime") Date endTime);

    List<BatteryOrder> findByCabinetId(@Param("agentId") Integer agentId,
                                @Param("cabinetId") String cabinetId,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    List<BatteryOrder> findBatteryListByCustomer(@Param("agentId") Integer agentId,
                                                 @Param("batteryId") String batteryId,
                                                 @Param("customerId") Long customerId);

    int findCountByCabinetId(@Param("agentId") Integer agentId,
                             @Param("cabinetId") String cabinetId,
                             @Param("orderStatus") Integer orderStatus);

    int findActiveUserCountByCabinetId(@Param("agentId") Integer agentId,
                                       @Param("cabinetId") String cabinetId,
                                       @Param("orderStatus") List<Integer> orderStatus);

    List<BatteryOrder> findList(@Param("agentId") Integer agentId,
                                @Param("keyword") String keyword,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    List<BatteryOrder> findByBatteryList(@Param("agentId") Integer agentId,
                                         @Param("batteryId") String batteryId,
                                         @Param("idAndName") String idAndName,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    List<BatteryOrder> findByBattery(@Param("agentId") Integer agentId,
                                     @Param("shopId") String shopId,
                                     @Param("batteryId") String batteryId,
                                     @Param("idAndName") String idAndName,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    List<BatteryOrder> findByShopBatteryList(@Param("agentId") Integer agentId,
                                             @Param("batteryId") String batteryId,
                                             @Param("cabinetList") List cabinetList,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    List<BatteryOrder> findListByShop(@Param("shopId") String shopId,
                                      @Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    int findTakeTimeoutCount(@Param("agentId") Integer agentId, @Param("orderStatus") Integer orderStatus, @Param("createTime") Date createTime );

    List<BatteryOrder> findTakeTimeout(@Param("agentId") Integer agentId, @Param("orderStatus") Integer orderStatus, @Param("createTime") Date createTime,
                                       @Param("offset") int offset, @Param("limit") int limit);

    int complete(@Param("id") String id,
                 @Param("completeTime") Date completeTime,
                 @Param("payType") int payType,
                 @Param("toStatus") int toStatus);

    int updateBattery(@Param("id") String id, @Param("batteryId") String batteryId);

    int countShopTodayOrderNum(@Param("shopId") String shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
