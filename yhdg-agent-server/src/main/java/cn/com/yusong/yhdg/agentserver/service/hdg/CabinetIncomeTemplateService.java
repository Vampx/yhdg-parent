package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetIncomeTemplateMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CabinetIncomeTemplateService extends AbstractService {

    @Autowired
    CabinetIncomeTemplateMapper cabinetIncomeTemplateMapper;
    @Autowired
    AgentMapper agentMapper;

    public Page findPage(CabinetIncomeTemplate search) {
        Page page = search.buildPage();
        page.setTotalItems(cabinetIncomeTemplateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CabinetIncomeTemplate> list = cabinetIncomeTemplateMapper.findPageResult(search);
        for(CabinetIncomeTemplate template : list) {
            template.setAgentName(agentMapper.find(template.getAgentId()).getAgentName());
        }
        page.setResult(list);
        return page;
    }

    public CabinetIncomeTemplate findByAgentId(int agentId) {
        return cabinetIncomeTemplateMapper.findByAgentId(agentId);
    }

    public CabinetIncomeTemplate find(int id) {
        return cabinetIncomeTemplateMapper.find(id);
    }

    public ExtResult create(CabinetIncomeTemplate entity) {
        int count = cabinetIncomeTemplateMapper.findByAgentIdCount(entity.getAgentId());
        if (count > 0) {
            return ExtResult.failResult("此运营商已有模板");
        }
        entity.setCreateTime(new Date());
        if (cabinetIncomeTemplateMapper.insert(entity) == 0) {
            return ExtResult.failResult("添加失败");
        }
        return ExtResult.successResult();
    }

    public ExtResult update(CabinetIncomeTemplate eitity) {
        if (cabinetIncomeTemplateMapper.update(eitity) == 0) {
            return ExtResult.failResult("修改失败");
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(int id) {
        if (cabinetIncomeTemplateMapper.delete(id) == 0) {
            return ExtResult.failResult("修改失败");
        }
        return ExtResult.successResult();
    }
}
