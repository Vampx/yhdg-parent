package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentInsuranceOrderMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.AccreditService;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentInsuranceOrderService extends AccreditService {

    @Autowired
    RentInsuranceOrderMapper RentInsuranceOrderMapper;

    public RentInsuranceOrder find(String id) {
        return RentInsuranceOrderMapper.find(id);
    }

    public RentInsuranceOrder findByCustomerId(long customerId, Integer batteryType, Integer status) {
        return RentInsuranceOrderMapper.findByCustomerId(customerId, batteryType, status);
    }

    public List<RentInsuranceOrder> findList(Integer agentId, long customerId, Integer status, int offset, int limit) {
        return RentInsuranceOrderMapper.findList(agentId, customerId, status, offset, limit);
    }

    public List<RentInsuranceOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return RentInsuranceOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

}
