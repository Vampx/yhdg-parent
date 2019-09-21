package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.BigContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BigContentService {
    @Autowired
    BigContentMapper bigContentMapper;

    public String find(int type, long id) {
        return bigContentMapper.find(type, id);
    }
}
