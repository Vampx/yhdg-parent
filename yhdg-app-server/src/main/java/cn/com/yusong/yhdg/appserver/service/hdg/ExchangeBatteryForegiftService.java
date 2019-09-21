package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetBatteryTypeMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeBatteryForegiftMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
@Service
public class ExchangeBatteryForegiftService  extends AbstractService {
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchngebatteryForegiftMapper;

    public ExchangeBatteryForegift find(Long id){
        return exchngebatteryForegiftMapper.find(id);
    }

    public List<ExchangeBatteryForegift> findByBatteryType (int agentId, int batteryType){
        return exchngebatteryForegiftMapper.findByBatteryType(agentId, batteryType);
    }

    public List<ExchangeBatteryForegift> findByCabinet (String cabinetId){
        //查询

        List<ExchangeBatteryForegift>  list =  exchngebatteryForegiftMapper.findByCabinet(cabinetId);
        if(list.size() > 0){
            for(ExchangeBatteryForegift exchangeBatteryForegift : list){
                SystemBatteryType type = findBatteryType(exchangeBatteryForegift.getBatteryType());
                if(type != null){
                    exchangeBatteryForegift.setTypeName(type.getTypeName());
                }
            }
        }
        return list;
    }

    public List<ExchangeBatteryForegift> findByAgent(int agentId, Integer batteryType) {
        List<ExchangeBatteryForegift>  list =  exchngebatteryForegiftMapper.findByAgent(agentId, batteryType);
        if(list.size() > 0){
            for(ExchangeBatteryForegift exchangeBatteryForegift : list){
                exchangeBatteryForegift.setTypeName(findBatteryType(exchangeBatteryForegift.getBatteryType()).getTypeName());
            }
        }
        return list;
    }
}
