package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;

    public Customer findByMpOpenId(int partnerId, String mpOpenId) {
        return customerMapper.findByMpOpenId(partnerId, mpOpenId);
    }

    public Customer findByFwOpenId(int partnerId, String fwOpenId) {
        return customerMapper.findByFwOpenId(partnerId, fwOpenId);
    }

    public int updateAuthFacePath(long id, String authFacePath) {
        return customerMapper.updateAuthFacePath(id, authFacePath);
    }

    public int updateCertification(long id, String fullname, String idCard, int authStatus) {
        return customerMapper.updateCertification(id, fullname, idCard, authStatus);
    }

    public int bindCompany(long id, String agentCompanyId) {
        return customerMapper.bindCompany(id, agentCompanyId);
    }
}
