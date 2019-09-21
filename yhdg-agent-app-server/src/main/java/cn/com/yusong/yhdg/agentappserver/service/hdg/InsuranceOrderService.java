package cn.com.yusong.yhdg.agentappserver.service.hdg;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InsuranceOrderService {

    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;

    public List<InsuranceOrder> findIncrement(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return insuranceOrderMapper.findIncrement(agentId, status, beginTime, endTime, offset, limit);
    }

    public List<InsuranceOrder> findRefund(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return insuranceOrderMapper.findRefund(agentId, status, beginTime, endTime, offset, limit);
    }

    public InsuranceOrder findByCustomerId(long customerId, Integer batteryType, Integer status) {
        return insuranceOrderMapper.findByCustomerId(customerId, batteryType, status);
    }
}
