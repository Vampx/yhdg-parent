package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerExchangeBatteryMapper  extends MasterMapper {
    List<CustomerExchangeBattery> findByCustomerId(@Param("customerId") Long customerId);

    int exists(@Param("customerId") long customerId);

    int insert(CustomerExchangeBattery customerExchangeBattery);

    int updateVehicleInfo(@Param("customerId")Long customerId,@Param("batteryId")String batteryId,@Param("batteryType")Integer batteryType,@Param("batteryOrderId")String batteryOrderId);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

    int updateBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

    int deleteByCustomerId(@Param("customerId") long customerId);
}
