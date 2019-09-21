package cn.com.yusong.yhdg.vehicleserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.domain.zc.VehicleOnlineStats;
import cn.com.yusong.yhdg.vehicleserver.persistence.zc.VehicleMapper;
import cn.com.yusong.yhdg.vehicleserver.persistence.zc.VehicleOnlineStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VehicleOnlineStatsService {
    @Autowired
    VehicleOnlineStatsMapper vehicleOnlineStatsMapper;
    @Autowired
    VehicleMapper vehicleMapper;

    public void begin(int id) {
        boolean ok = false;
        VehicleOnlineStats stats = vehicleOnlineStatsMapper.findMaxRecord(id);
        if(stats != null && stats.getEndTime() == null) {
            vehicleOnlineStatsMapper.updateEndTime(id, new Date());
            ok = true;
        } else if (stats == null || stats.getEndTime() != null) {
            ok = true;
        }

        if (ok) {
            stats = new VehicleOnlineStats();
            stats.setVehicleId(id);
            stats.setBeginTime(new Date());
            vehicleOnlineStatsMapper.insert(stats);
        }
    }

    public void end(String vinNo) {
        Vehicle vehicle = vehicleMapper.findByVinNo(vinNo);
        vehicleOnlineStatsMapper.updateEndTime(vehicle.getId(), new Date());
        vehicleMapper.updateOnline(vehicle.getId(), ConstEnum.Flag.FALSE.getValue());
    }
}
