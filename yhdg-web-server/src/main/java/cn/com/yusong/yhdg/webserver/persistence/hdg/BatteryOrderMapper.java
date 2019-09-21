package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.entity.OrderDateCount;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BatteryOrderMapper extends MasterMapper {
    int findCountByTakeCabinet(@Param("takeCabinetId") String takeCabinetId);

    int findCountByPutCabinet(@Param("putCabinetId") String putCabinetId);

    int findCountByAgentCompany(@Param("agentCompanyId") String agentCompanyId);

    int findPageCount(BatteryOrder batteryOrder);

    List<BatteryOrder> findPageResult(BatteryOrder batteryOrder);

    List<BatteryOrder> findList(long customerId);

    int updateBatteryOrder(@Param("id") String id,
                           @Param("customerId") long customerId,
                           @Param("customerMobile") String customerMobile,
                           @Param("customerFullname") String customerFullname,
                           @Param("payType") Integer payType,
                           @Param("createTime") Date createTime);

    int findPageForBalanceCount(BatteryOrder batteryOrder);

    List findPageForBalanceResult(BatteryOrder batteryOrder);

    int hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    List<OrderDateCount> findDateOrderCount(@Param("agentId") Integer agentId,
                                            @Param("beginPrefixId") String beginPrefixId,
                                            @Param("endPrefixId") String endPrefixId,
                                            @Param("takeCabinetId") String takeCabinetId);

    BatteryOrder find(String id);

    int findOrderCount(@Param("agentId") Integer agentId,
                       @Param("beginPrefixId") String beginPrefixId,
                       @Param("endPrefixId") String endPrefixId,
                       @Param("takeCabinetId") String takeCabinetId);

    int updateStatus(@Param("id") String id,
                     @Param("refundStatus") int refundStatus,
                     @Param("refundMoney") int refundMoney,
                     @Param("refundTime") Date refundTime,
                     @Param("refundReason") String refundReason);

    int complete(@Param("id") String id, @Param("completeTime") Date completeTime, @Param("payType") int payType, @Param("toStatus") int toStatus);

    int updateBattery(@Param("id") String id, @Param("batteryId") String batteryId);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int deleteByBatteryId(@Param("batteryId") String batteryId);

    int updateFaultLog(@Param("property") String property, @Param("value") Object value);

    int insert(BatteryOrder batteryOrder);
}
