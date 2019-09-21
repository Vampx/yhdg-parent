package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceAgentCompanyMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPriceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceAgentCompany;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceShop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipRentPriceAgentCompanyService extends AbstractService {

    @Autowired
    VipRentPriceAgentCompanyMapper vipRentPriceAgentCompanyMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    VipRentPriceMapper vipRentPriceMapper;

    public List<VipRentPriceAgentCompany> findListByPriceId(Long priceId) {
        List<VipRentPriceAgentCompany> list = vipRentPriceAgentCompanyMapper.findListByPriceId(priceId);
        for (VipRentPriceAgentCompany vipRentPriceAgentCompany : list) {
            AgentCompany agentCompany = agentCompanyMapper.find(vipRentPriceAgentCompany.getAgentCompanyId());
            vipRentPriceAgentCompany.setCompanyName(agentCompany.getCompanyName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipRentPriceAgentCompany entity) {
        String[] agentCompanyIdArr = entity.getIds().split(",");
        for (String agentCompanyId : agentCompanyIdArr) {
            VipRentPriceAgentCompany vipPriceAgentCompany = vipRentPriceAgentCompanyMapper.findByPriceId(entity.getPriceId(), agentCompanyId);
            if (vipPriceAgentCompany != null) {
                return ExtResult.failResult("包含已存在的运营公司");
            }
            VipRentPriceAgentCompany vrpac = new VipRentPriceAgentCompany();
            vrpac.setPriceId(entity.getPriceId());
            vrpac.setAgentCompanyId(agentCompanyId);
            vipRentPriceAgentCompanyMapper.insert(vrpac);
        }
        List<VipRentPriceAgentCompany> companyList = vipRentPriceAgentCompanyMapper.findListByPriceId(entity.getPriceId());
        vipRentPriceMapper.updateAgentCompanyCount(entity.getPriceId(), companyList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipRentPriceAgentCompanyMapper.delete(id);
        List<VipRentPriceAgentCompany> companyList = vipRentPriceAgentCompanyMapper.findListByPriceId(priceId);
        vipRentPriceMapper.updateAgentCompanyCount(priceId, companyList.size());
        return ExtResult.successResult();
    }
}
