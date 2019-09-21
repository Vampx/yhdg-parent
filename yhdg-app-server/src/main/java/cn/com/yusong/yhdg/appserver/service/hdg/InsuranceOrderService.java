package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceOrderService extends AccreditService {

    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;

    public InsuranceOrder find(String id) {
        return insuranceOrderMapper.find(id);
    }

    public InsuranceOrder findByCustomerId(long customerId, Integer batteryType, Integer status) {
        return insuranceOrderMapper.findByCustomerId(customerId, batteryType, status);
    }

    public List<InsuranceOrder> findList(Integer agentId, long customerId, Integer status, int offset, int limit) {
        return insuranceOrderMapper.findList(agentId, customerId, status, offset, limit);
    }

    public List<InsuranceOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return insuranceOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

}
