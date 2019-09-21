package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetProperty;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CabinetPropertyService {

    @Autowired
    CabinetPropertyMapper cabinetPropertyMapper;

    public List<CabinetProperty> findByCabinet(String cabinetId) {
        return cabinetPropertyMapper.findByCabinet(cabinetId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int insert(String cabinetId, int[] active, String[] property, String[] value) {
        int effect = 0;
        cabinetPropertyMapper.deleteByCabinet(cabinetId);

        if (active != null && property != null && value != null) {

            for (int i = 0; i < active.length; i++) {
                CabinetProperty record = new CabinetProperty();
                record.setOrderNum(i + 1);
                record.setCabinetId(cabinetId);
                record.setIsActive(active[i]);
                record.setPropertyName(property[i]);
                record.setPropertyValue(value[i]);

                cabinetPropertyMapper.insert(record);

                effect++;
            }
        }

        return effect;
    }
}
