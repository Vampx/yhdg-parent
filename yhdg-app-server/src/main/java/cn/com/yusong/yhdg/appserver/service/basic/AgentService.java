package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    @Autowired
    AgentMapper agentMapper;

    public Agent find(int id) {
        return agentMapper.find(id);
    }

    public List<Integer> findByPartner(Integer parentId) {
        return agentMapper.findByPartnerId(parentId);
    }

    public List<Integer> findByWeixinmp(Integer weixinmpId) {
        return agentMapper.findByWeixinmp(weixinmpId);
    }

    public List<Integer> findByAlipayfw(Integer alipayfwId) {
        return agentMapper.findByAlipayfw(alipayfwId);
    }

    public List<Integer> findByPhoneapp(Integer phoneappId) {
        return agentMapper.findByPhoneapp(phoneappId);
    }
}
