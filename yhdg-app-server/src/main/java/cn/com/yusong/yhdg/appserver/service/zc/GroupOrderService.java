package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehiclePeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupOrderService extends AbstractService {

    @Autowired
    GroupOrderMapper groupOrderMapper;


    public GroupOrder find(String id) {
        return groupOrderMapper.find(id);
    }

    public GroupOrder findByVehicleForegiftId(String vehicleForegiftId) {
        return groupOrderMapper.findByVehicleForegiftId(vehicleForegiftId);
    }

    public GroupOrder findByVehiclePeriodId(String vehiclePeriodId) {
        return groupOrderMapper.findByVehiclePeriodId(vehiclePeriodId);
    }

    public GroupOrder findByBatteryForegiftId(String batteryForegiftId) {
        return groupOrderMapper.findByBatteryForegiftId(batteryForegiftId);
    }

    public GroupOrder findByBatteryPeriodId(String batteryPeriodId) {
        return groupOrderMapper.findByBatteryPeriodId(batteryPeriodId);
    }

    public List<GroupOrder> findList(long customerId, int offset, int limit, Integer status) {
        return groupOrderMapper.findList(customerId, offset, limit, status);
    }

    public List<GroupOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return groupOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

}