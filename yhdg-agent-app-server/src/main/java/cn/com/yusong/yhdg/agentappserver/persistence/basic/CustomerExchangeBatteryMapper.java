package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerExchangeBatteryMapper extends MasterMapper {
    List<CustomerExchangeBattery> findByCustomerId(@Param("customerId") Long customerId);

    CustomerExchangeBattery findByBatteryId(@Param("batteryId") String batteryId);

    int exists(@Param("customerId") long customerId);

    int insert(CustomerExchangeBattery customerExchangeBattery);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

    int updateBattery(@Param("customerId") long customerId, @Param("oldBatteryId") String oldBatteryId, @Param("newBatteryId") String newBatteryId);

    int deleteByCustomerId(@Param("customerId") long customerId);
}
