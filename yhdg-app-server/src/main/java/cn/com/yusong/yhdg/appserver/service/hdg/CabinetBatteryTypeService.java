package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetBatteryTypeMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabinetBatteryTypeService extends AbstractService {

    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;

    public List<CabinetBatteryType> findListByCabinet(String cabinetId) {
        List<CabinetBatteryType> list = cabinetBatteryTypeMapper.findListByCabinet(cabinetId);
        if(list.size() > 0){
            for(CabinetBatteryType cabinetBatteryType : list){
                cabinetBatteryType.setTypeName(findBatteryType(cabinetBatteryType.getBatteryType()).getTypeName());
            }
        }
        return list;
    }
}
