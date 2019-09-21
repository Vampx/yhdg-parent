package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBatteryType;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CabinetBatteryTypeService extends AbstractService {

    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    CabinetMapper cabinetMapper;

    public List<CabinetBatteryType> findListByBatteryType(Integer batteryType, Integer agentId) {
        List<CabinetBatteryType> list = cabinetBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
        for (CabinetBatteryType vipPriceCabinet : list) {
            Cabinet cabinet = cabinetMapper.find(vipPriceCabinet.getCabinetId());
            vipPriceCabinet.setCabinetName(cabinet.getCabinetName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(CabinetBatteryType entity) {
        String[] cabinetIdArr = entity.getIds().split(",");
        for (String cabinetId : cabinetIdArr) {
            List<CabinetBatteryType> list = cabinetBatteryTypeMapper.findListByCabinet(cabinetId);
            if (list.size() > 0) {
                return ExtResult.failResult("包含已存在的柜子");
            }
            CabinetBatteryType cct = new CabinetBatteryType();
            cct.setBatteryType(entity.getBatteryType());
            cct.setCabinetId(cabinetId);
            cabinetBatteryTypeMapper.insert(cct);
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(String cabinetId, Integer batteryType) {
        cabinetBatteryTypeMapper.delete(cabinetId, batteryType);
        return ExtResult.successResult();
    }
}
