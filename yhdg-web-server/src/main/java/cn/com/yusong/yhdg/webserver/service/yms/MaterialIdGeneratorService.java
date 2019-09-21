package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.MaterialIdGenerator;
import cn.com.yusong.yhdg.webserver.persistence.yms.MaterialIdGeneratorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialIdGeneratorService {
    @Autowired
    MaterialIdGeneratorMapper materialIdGeneratorMapper;

    public long newId() {
        MaterialIdGenerator id = new MaterialIdGenerator();
        materialIdGeneratorMapper.insert(id);
        return id.getId();
    }
}
