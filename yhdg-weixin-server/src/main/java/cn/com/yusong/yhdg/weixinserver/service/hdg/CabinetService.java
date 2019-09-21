package cn.com.yusong.yhdg.weixinserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.weixinserver.persistence.hdg.CabinetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetService {
    @Autowired
    CabinetMapper cabinetMapper;

    public Cabinet find(String id) {
        return cabinetMapper.find(id);
    }
}
