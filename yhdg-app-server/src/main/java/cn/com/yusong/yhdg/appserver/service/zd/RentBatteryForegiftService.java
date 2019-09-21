package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentBatteryForegiftMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
@Service
public class RentBatteryForegiftService extends AbstractService {
    @Autowired
    RentBatteryForegiftMapper rentBatteryForegiftMapper;

    public RentBatteryForegift find(Long id){
        return rentBatteryForegiftMapper.find(id);
    }

    public List<RentBatteryForegift> findByAgent(int agentId, Integer batteryType) {
        List<RentBatteryForegift>  list =  rentBatteryForegiftMapper.findByAgent(agentId, batteryType);
        if(list.size() > 0){
            for(RentBatteryForegift rentBatteryForegift : list){
                rentBatteryForegift.setTypeName(findBatteryType(rentBatteryForegift.getBatteryType()).getTypeName());
            }
        }
        return list;
    }

    public RentBatteryForegift findOneByAgentId(int agentId) {
        return rentBatteryForegiftMapper.findOneByAgentId(agentId);
    }

}
