package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerExchangeBatteryMapper extends MasterMapper {
    List<CustomerExchangeBattery> findListByCustomer(@Param("customerId") long customerId);

    int exists(@Param("customerId") long customerId);

    int insert(CustomerExchangeBattery customerExchangeBattery);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String battertId);
}
