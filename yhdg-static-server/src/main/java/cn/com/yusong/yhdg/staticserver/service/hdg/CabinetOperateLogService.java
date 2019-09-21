package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.CabinetOperateLogMapper;
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

