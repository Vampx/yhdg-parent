package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDynamicCodeCustomer;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetDynamicCodeCustomerMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CabinetDynamicCodeCustomerService extends AbstractService {

    @Autowired
    CabinetDynamicCodeCustomerMapper cabinetDynamicCodeCustomerMapper;

    public CabinetDynamicCodeCustomer find(String cabinetId, Long customerId) {
        return cabinetDynamicCodeCustomerMapper.find(cabinetId, customerId);
    }

}
