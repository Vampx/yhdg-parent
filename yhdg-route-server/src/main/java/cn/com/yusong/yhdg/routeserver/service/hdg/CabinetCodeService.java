package cn.com.yusong.yhdg.routeserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import cn.com.yusong.yhdg.routeserver.persistence.hdg.CabinetCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetCodeService {
    @Autowired
    CabinetCodeMapper cabinetCodeMapper;

    public CabinetCode find(String id) {
        return cabinetCodeMapper.find(id);
    }
}
