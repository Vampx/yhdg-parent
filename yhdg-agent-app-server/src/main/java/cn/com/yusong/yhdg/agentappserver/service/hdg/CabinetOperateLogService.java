package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetOperateLogMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CabinetOperateLogService extends AbstractService {
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;

    public void insert(CabinetOperateLog log) {
        cabinetOperateLogMapper.insert(log);
    }
}
