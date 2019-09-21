package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.CabinetCodeMapper;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
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
