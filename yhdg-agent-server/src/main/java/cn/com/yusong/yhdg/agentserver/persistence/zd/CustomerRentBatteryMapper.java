package cn.com.yusong.yhdg.agentserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerRentBatteryMapper extends MasterMapper {
    List<CustomerRentBattery> findByCustomerId(@Param("customerId") Long customerId);

    int exists(@Param("customerId") long customerId);

    int insert(CustomerRentBattery customerExchangeBattery);

    int updateVehicleInfo(@Param("customerId") Long customerId, @Param("batteryId") String batteryId, @Param("batteryType") Integer batteryType, @Param("batteryOrderId") String batteryOrderId);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

    int updateBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

    int deleteByCustomerId(@Param("customerId") long customerId);
}
