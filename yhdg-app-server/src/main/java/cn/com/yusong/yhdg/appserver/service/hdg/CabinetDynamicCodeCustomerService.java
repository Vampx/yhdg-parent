package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDynamicCodeCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetDynamicCodeCustomerService extends AbstractService {

    @Autowired
    CabinetDynamicCodeCustomerMapper cabinetDynamicCodeCustomerMapper;

    public CabinetDynamicCodeCustomer find(String cabinetId, Long customerId) {
        return cabinetDynamicCodeCustomerMapper.find(cabinetId, customerId);
    }

    public int insert(CabinetDynamicCodeCustomer cabinetDynamicCodeCustomer) {
        return cabinetDynamicCodeCustomerMapper.insert(cabinetDynamicCodeCustomer);
    }
}
