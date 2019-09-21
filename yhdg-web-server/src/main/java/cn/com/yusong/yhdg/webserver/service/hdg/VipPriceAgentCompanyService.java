package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceAgentCompany;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceAgentCompany;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceAgentCompanyMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VipPriceAgentCompanyService extends AbstractService {

    @Autowired
    VipPriceAgentCompanyMapper vipPriceAgentCompanyMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;

    public List<VipPriceAgentCompany> findListByPriceId(Long priceId) {
        List<VipPriceAgentCompany> list = vipPriceAgentCompanyMapper.findListByPriceId(priceId);
        for (VipPriceAgentCompany vipPriceAgentCompany : list) {
            AgentCompany agentCompany = agentCompanyMapper.find(vipPriceAgentCompany.getAgentCompanyId());
            vipPriceAgentCompany.setCompanyName(agentCompany.getCompanyName());
        }
        return list;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(VipPriceAgentCompany entity) {
        String[] agentCompanyIdArr = entity.getIds().split(",");
        for (String agentCompanyId : agentCompanyIdArr) {
            VipPriceAgentCompany vipPriceAgentCompany = vipPriceAgentCompanyMapper.findByPriceId(entity.getPriceId(), agentCompanyId);
            if (vipPriceAgentCompany != null) {
                return ExtResult.failResult("包含已存在的运营公司");
            }
            VipPriceAgentCompany vpac = new VipPriceAgentCompany();
            vpac.setPriceId(entity.getPriceId());
            vpac.setAgentCompanyId(agentCompanyId);
            vipPriceAgentCompanyMapper.insert(vpac);
        }
        List<VipPriceAgentCompany> agentCompanyList = vipPriceAgentCompanyMapper.findListByPriceId(entity.getPriceId());
        vipPriceMapper.updateAgentCompanyCount(entity.getPriceId(), agentCompanyList.size());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id, Long priceId) {
        vipPriceAgentCompanyMapper.delete(id);
        List<VipPriceAgentCompany> agentCompanyList = vipPriceAgentCompanyMapper.findListByPriceId(priceId);
        vipPriceMapper.updateAgentCompanyCount(priceId, agentCompanyList.size());
        return ExtResult.successResult();
    }
}
