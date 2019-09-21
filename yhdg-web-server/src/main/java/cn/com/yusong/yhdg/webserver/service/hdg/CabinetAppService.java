package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetAppMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetAppService extends AbstractService {
    @Autowired
    CabinetAppMapper cabinetAppMapper;

    public int delete(String cabinetId) {
        return cabinetAppMapper.delete(cabinetId);
    }
}
