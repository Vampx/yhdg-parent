package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentForegiftRefundMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentForegiftRefundService extends AbstractService {

    @Autowired
    private AgentForegiftRefundMapper agentForegiftRefundMapper;

}
