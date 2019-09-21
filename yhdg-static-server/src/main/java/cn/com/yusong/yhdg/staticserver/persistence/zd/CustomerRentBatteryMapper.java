package cn.com.yusong.yhdg.staticserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerRentBatteryMapper extends MasterMapper {
    List<CustomerRentBattery> findListByCustomer(@Param("customerId") long customerId);

    int existsByCustomer(@Param("customerId") long customerId, @Param("batteryId") String battertId);

    int exists(@Param("customerId") long customerId);

    int insert(CustomerRentBattery customerRentBattery);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String battertId);
}
