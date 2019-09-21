package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceCustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceCustomer;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipRentPriceCustomerService extends AbstractService {

    @Autowired
    VipRentPriceMapper vipPriceMapper;
    @Autowired
    VipRentPriceCustomerMapper vipRentPriceCustomerMapper;
    @Autowired
    CustomerMapper customerMapper;

    public List<VipRentPriceCustomer> findListByPriceId(Long priceId) {
        return vipRentPriceCustomerMapper.findListByPriceId(priceId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipRentPriceCustomer entity) {
        VipRentPriceCustomer vipPriceCustomer = vipRentPriceCustomerMapper.findByAgentIdAndMobile(entity.getAgentId(), entity.getMobile());
        if (vipPriceCustomer != null) {
            return ExtResult.failResult("该骑手手机号已存在");
        }
        VipRentPriceCustomer cct = new VipRentPriceCustomer();
        cct.setMobile(entity.getMobile());
        cct.setPriceId(entity.getPriceId());
        cct.setCreateTime(new Date());
        vipRentPriceCustomerMapper.insert(cct);
        List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(entity.getPriceId());
        vipPriceMapper.updateCustomerCount(entity.getPriceId(), customerList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipRentPriceCustomerMapper.delete(id);
        List<VipRentPriceCustomer> customerList = vipRentPriceCustomerMapper.findListByPriceId(priceId);
        vipPriceMapper.updateCustomerCount(priceId, customerList.size());
        return ExtResult.successResult();
    }
}
