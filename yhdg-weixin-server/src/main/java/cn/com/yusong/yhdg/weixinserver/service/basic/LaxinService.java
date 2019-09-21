package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.LaxinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaxinService {
    @Autowired
    LaxinMapper laxinMapper;

    public Laxin find(long id) {
        return laxinMapper.find(id);
    }

    public Laxin findByAgentMobile(int agentId, String mobile) {
        return laxinMapper.findByAgentMobile(agentId, mobile);
    }
}
