package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceCustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipPriceCustomerService extends AbstractService {

    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    VipPriceCustomerMapper vipPriceCustomerMapper;
    @Autowired
    CustomerMapper customerMapper;

    public List<VipPriceCustomer> findListByPriceId(Long priceId) {
        List<VipPriceCustomer> customerList =  vipPriceCustomerMapper.findListByPriceId(priceId);
        for (VipPriceCustomer customer : customerList) {
            Customer customerMobile = customerMapper.findByMobile(customer.getMobile());
            if (customerMobile != null) {
                customer.setCustomerFullname(customerMobile.getFullname());
            }
        }
        return customerList;
    }

    public VipPriceCustomer findByAgentIdAndMobile(Integer agentId, String mobile) {
        return vipPriceCustomerMapper.findByAgentIdAndMobile(agentId, mobile);
    }

    public int insert(VipPriceCustomer vipPriceCustomer) {
        return vipPriceCustomerMapper.insert(vipPriceCustomer);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPriceCustomer entity) {
        VipPriceCustomer vipPriceCustomer = vipPriceCustomerMapper.findByAgentIdAndMobile(entity.getAgentId(), entity.getMobile());
        if (vipPriceCustomer != null) {
            return ExtResult.failResult("该骑手手机号已存在");
        }
        VipPriceCustomer cct = new VipPriceCustomer();
        cct.setMobile(entity.getMobile());
        cct.setPriceId(entity.getPriceId());
        cct.setCreateTime(new Date());
        vipPriceCustomerMapper.insert(cct);
        List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(entity.getPriceId());
        vipPriceMapper.updateCustomerCount(entity.getPriceId(), customerList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipPriceCustomerMapper.delete(id);
        List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(priceId);
        vipPriceMapper.updateCustomerCount(priceId, customerList.size());
        return ExtResult.successResult();
    }
}
