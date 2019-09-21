package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.VipPriceCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
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
        return vipPriceCustomerMapper.findListByPriceId(priceId);
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
