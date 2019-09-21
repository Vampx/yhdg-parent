
package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerExchangeInfoService extends AbstractService {
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public CustomerExchangeInfo find(long id) {
        return customerExchangeInfoMapper.find(id);
    }

    public int findByBalanceCabinetId(String balanceCabinetId) {
        return customerExchangeInfoMapper.findByBalanceCabinetId(balanceCabinetId);
    }

    public int findCountByAgentId(Integer agentId) {
        return customerExchangeInfoMapper.findCountByAgentId(agentId);
    }
}


