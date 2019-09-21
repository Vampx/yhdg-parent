package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.CabinetCodeMapper;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetCodeService {
    @Autowired
    CabinetCodeMapper subcabinetCodeMapper;

    public CabinetCode find(String id) {
        return subcabinetCodeMapper.find(id);
    }
}
