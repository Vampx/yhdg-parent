package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentInsuranceOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RentInsuranceOrderService extends AbstractService {
    @Autowired
    RentInsuranceOrderMapper rentInsuranceOrderMapper;

    public List<RentInsuranceOrder> findIncrement(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return rentInsuranceOrderMapper.findIncrement(agentId, status, beginTime, endTime, offset, limit);
    }

    public List<RentInsuranceOrder> findRefund(Integer agentId, Integer status, Date beginTime, Date endTime, int offset, int limit) {
        return rentInsuranceOrderMapper.findRefund(agentId, status, beginTime, endTime, offset, limit);
    }

    public RentInsuranceOrder findByCustomerId(Long customerId, Integer batteryType, int status) {
        return rentInsuranceOrderMapper.findByCustomerId(customerId, batteryType, status);
    }
}
