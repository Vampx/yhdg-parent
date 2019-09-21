package cn.com.yusong.yhdg.routeserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetSimReplaceRecord;
import cn.com.yusong.yhdg.routeserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.routeserver.persistence.hdg.CabinetSimReplaceRecordMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CabinetService {
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetSimReplaceRecordMapper cabinetSimReplaceRecordMapper;

    public Cabinet find(String id) {
        return cabinetMapper.find(id);
    }

    public int updateSimMemo(String id, String oldCode, String newCode) {
        int effect = cabinetMapper.updateSimMemo(id, newCode);

        if(effect > 0 && StringUtils.isNotEmpty(oldCode)) {
            CabinetSimReplaceRecord replaceRecord = new CabinetSimReplaceRecord();
            replaceRecord.setCabinetId(id);
            replaceRecord.setOldCode(oldCode);
            replaceRecord.setNewCode(newCode);
            replaceRecord.setOperator("system");
            replaceRecord.setCreateTime(new Date());
            cabinetSimReplaceRecordMapper.insert(replaceRecord);
        }

        return effect;
    }
}
