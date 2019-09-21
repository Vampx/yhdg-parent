package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    @Autowired
    AreaMapper areaMapper;

    public List<Area> findAll() {
        return areaMapper.findAll();
    }
}
