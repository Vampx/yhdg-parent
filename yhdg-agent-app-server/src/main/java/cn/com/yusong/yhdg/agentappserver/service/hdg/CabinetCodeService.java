package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetCodeMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetCodeService extends AbstractService {

    @Autowired
    CabinetCodeMapper cabinetCodeMapper;

    public CabinetCode find(String code) {
        return cabinetCodeMapper.findByCode(code);
    }
}
