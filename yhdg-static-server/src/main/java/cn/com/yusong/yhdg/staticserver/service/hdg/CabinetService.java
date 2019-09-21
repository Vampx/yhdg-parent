package cn.com.yusong.yhdg.staticserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.staticserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.staticserver.service.basic.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetService extends AbstractService {
    @Autowired
    CabinetMapper cabinetMapper;

    public Cabinet find(String id) {
        return cabinetMapper.find(id);
    }
}
