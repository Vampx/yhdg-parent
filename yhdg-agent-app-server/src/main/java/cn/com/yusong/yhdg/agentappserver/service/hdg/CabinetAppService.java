package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetAppMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetAppService extends AbstractService {
    @Autowired
    CabinetAppMapper cabinetAppMapper;

    public int insert(int appId, String cabinetId) {
        return cabinetAppMapper.insert(appId, cabinetId);
    }
}
