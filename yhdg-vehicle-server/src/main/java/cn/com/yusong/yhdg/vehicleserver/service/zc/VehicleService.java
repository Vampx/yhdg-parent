package cn.com.yusong.yhdg.vehicleserver.service.zc;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.vehicleserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.vehicleserver.persistence.zc.VehicleMapper;
import cn.com.yusong.yhdg.vehicleserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VehicleService extends AbstractService {
    @Autowired
    VehicleMapper vehicleMapper;
    @Autowired
    AgentMapper agentMapper;

    public Vehicle findByVinNo(String vinNo){
        return vehicleMapper.findByVinNo(vinNo);
    }



    @Transactional(rollbackFor=Throwable.class)
    public Vehicle insertOrUpdate(String vinNo, String version) {
        Vehicle vehicle = vehicleMapper.findByVinNo(vinNo);

        if (vehicle == null) {
            /*车辆*/
            vehicle = new Vehicle();
            vehicle.setVinNo(vinNo);
            vehicle.setVersion(version);
            //vehicle.setModelId(1);
            vehicle.setAgentId(Constant.TEST_AGENT_ID);
            vehicle.setAgentName( agentMapper.find(Constant.TEST_AGENT_ID).getAgentName());
            vehicle.setActiveStatus(ConstEnum.Flag.TRUE.getValue());
            vehicle.setStatus(Vehicle.Status.NOT_USE.getValue());
            vehicle.setLockSwitch(Vehicle.LockSwitch.DISCHG_CLOSE.getValue());
            vehicle.setUpLineStatus(ConstEnum.Flag.FALSE.getValue());
            vehicle.setLockStatus(ConstEnum.Flag.FALSE.getValue());
            vehicle.setIsActive(ConstEnum.Flag.TRUE.getValue());
            vehicle.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            vehicle.setReportTime(new Date());
            vehicle.setCreateTime(new Date());
            vehicleMapper.insert(vehicle);
        }else{
            vehicle.setVersion(version);
            vehicle.setIsOnline(ConstEnum.Flag.TRUE.getValue());
            vehicle.setReportTime(new Date());
            vehicleMapper.update(vehicle);
        }

        return vehicle;
    }

    public void updateHeart(String vinNo) {
        Vehicle vehicle = vehicleMapper.findByVinNo(vinNo);
        if(vehicle  != null){
            vehicleMapper.updateHeart(vehicle.getId(), ConstEnum.Flag.TRUE.getValue(), new Date());
        }
    }


}
