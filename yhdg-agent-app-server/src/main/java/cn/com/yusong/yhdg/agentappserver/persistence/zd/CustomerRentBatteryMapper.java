package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerRentBatteryMapper extends MasterMapper {

    List<CustomerRentBattery> findByCustomerId(@Param("customerId") long customerId);

    int updateBattery(@Param("customerId") long customerId, @Param("oldBatteryId") String oldBatteryId, @Param("newBatteryId") String newBatteryId);

    int clearBattery(@Param("customerId") long customerId, @Param("batteryId") String batteryId);

}
