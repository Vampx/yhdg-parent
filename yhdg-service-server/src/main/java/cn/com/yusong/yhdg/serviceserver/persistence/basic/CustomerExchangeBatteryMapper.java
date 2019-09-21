package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerExchangeBatteryMapper extends MasterMapper {
    CustomerExchangeBattery findByBattery(@Param("batteryId") String batteryId);
    CustomerExchangeBattery findOneByCustomer(@Param("customerId") long customerId);
    int insert(CustomerExchangeBattery customerExchangeBattery);
    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String battertId);


}
