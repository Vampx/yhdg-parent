package cn.com.yusong.yhdg.frontserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.frontserver.persistence.hdg.CabinetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetService {
    @Autowired
    CabinetMapper cabinetMapper;

    public Cabinet findTerminal (String terminalId) {
        return cabinetMapper.findTerminal(terminalId);
    }

}
