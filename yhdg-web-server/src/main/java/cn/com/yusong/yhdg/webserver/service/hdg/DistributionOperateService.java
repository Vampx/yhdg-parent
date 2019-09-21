package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DistributionOperateService extends AbstractService {

    @Autowired
    DistributionOperateMapper distributionOperateMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    StationDistributionMapper stationDistributionMapper;

    public DistributionOperate find(long id) {
        return distributionOperateMapper.find(id);
    }

    public Page findPage(DistributionOperate search) {
        Page page = search.buildPage();
        page.setTotalItems(distributionOperateMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<DistributionOperate> list = distributionOperateMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult create(DistributionOperate distributionOperate) {
        distributionOperate.setCreateTime(new Date());
        Agent agent = agentMapper.find(distributionOperate.getAgentId());
        distributionOperate.setAgentName(agent.getAgentName());
        distributionOperate.setAgentCode(agent.getAgentCode());
        distributionOperateMapper.insert(distributionOperate);
        return DataResult.successResult();
    }

    public ExtResult update(DistributionOperate distributionOperate) {
        distributionOperateMapper.update(distributionOperate);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(long id) {
        List<StationDistribution> list = stationDistributionMapper.findListByOperateId(id);
        if(list.size() > 0) {
            return ExtResult.failResult("该运营体关联分成信息，不能删除。");
        }
        distributionOperateMapper.delete(id);
        return ExtResult.successResult();
    }
}
