package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.serviceserver.persistence.basic.AgentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    @Autowired
    AgentMapper agentMapper;

    public List<Integer> findAllId() {
        return agentMapper.findAllId();
    }

}
