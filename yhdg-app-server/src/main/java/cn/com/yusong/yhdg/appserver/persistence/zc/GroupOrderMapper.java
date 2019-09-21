package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface GroupOrderMapper extends MasterMapper {

    GroupOrder find(String id);

    GroupOrder findByVehicleForegiftId(@Param("vehicleForegiftId") String vehicleForegiftId);

    GroupOrder findByVehiclePeriodId(@Param("vehiclePeriodId") String vehiclePeriodId);

    GroupOrder findByBatteryForegiftId(@Param("batteryForegiftId") String batteryForegiftId);

    GroupOrder findByBatteryPeriodId(@Param("batteryRentId") String batteryRentId);

    GroupOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("modelId") int modelId);

    List<GroupOrder> findList(@Param("customerId") long customerId, @Param("offset") int offset, @Param("limit") int limit, @Param("status") Integer status);

    List<GroupOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    int payOk(@Param("id") String id, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    int updateRefund(@Param("id") String id,
                     @Param("applyRefundTime") Date applyRefundTime,
                     @Param("refundReason") String refundReason,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);

    int updateOrderInfo(@Param("id") String id, @Param("vehicleName") String vehicleName);

    int insert(GroupOrder groupOrder);

}
