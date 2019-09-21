package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetOperateLogMapper;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetOperateLogService {
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;

    public void insert(CabinetOperateLog log) {
        cabinetOperateLogMapper.insert(log);
    }
}
