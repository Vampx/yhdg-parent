package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetBatteryTypeMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetBatteryTypeService extends AbstractService {

    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;

    public int insert(String cabinetId, int batteryType) {
        int result = 0;
        if (cabinetBatteryTypeMapper.findAll(cabinetId, batteryType) == null) {
            CabinetBatteryType cabinetBatteryType = new CabinetBatteryType();
            cabinetBatteryType.setCabinetId(cabinetId);
            cabinetBatteryType.setBatteryType(batteryType);
            result = cabinetBatteryTypeMapper.insert(cabinetBatteryType);
        }
       return result;
    }
}
