package cn.com.yusong.yhdg.agentserver.service.yms;

import cn.com.yusong.yhdg.agentserver.persistence.yms.BigContentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalStrategyMapper;
import cn.com.yusong.yhdg.common.domain.yms.BigContent;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerminalStrategyService {
    @Autowired
    TerminalStrategyMapper terminalStrategyMapper;
    @Autowired
    BigContentMapper bigContentMapper;
    @Autowired
    TerminalMapper terminalMapper;

    public Page findPage(TerminalStrategy search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalStrategyMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(terminalStrategyMapper.findPageResult(search));
        return page;
    }

    public TerminalStrategy find(Long id) {
        return terminalStrategyMapper.find(id);
    }

    @Transactional(rollbackFor=Throwable.class)
    public int create(TerminalStrategy entity, String content) {
        entity.setVersion(1);
        int count = terminalStrategyMapper.insert(entity);

        BigContent bigContent = new BigContent();
        bigContent.setId(entity.getId());
        bigContent.setType(BigContent.Type.TERMINAL_STRATEGY.getValue());
        bigContent.setContent(content);
        bigContentMapper.insert(bigContent);
        return count;
    }

    @Transactional(rollbackFor=Throwable.class)
    public int update(TerminalStrategy entity, String content) {
        int count = terminalStrategyMapper.update(entity);

        BigContent bigContent = new BigContent();
        bigContent.setId(entity.getId());
        bigContent.setType(BigContent.Type.TERMINAL_STRATEGY.getValue());
        bigContent.setContent(content);
        if(bigContentMapper.update(bigContent) == 0) {
            bigContentMapper.insert(bigContent);
        }
        return count;
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult delete(Long id) {
        ExtResult result = hasRef(id);
        if (!result.isSuccess()){
            return  result;
        }
        bigContentMapper.delete(BigContent.Type.TERMINAL_STRATEGY.getValue(), id);
        terminalStrategyMapper.delete(id);
        return  result;
    }

    public ExtResult hasRef(long strategyId){
        String terminalId = terminalMapper.hasRecordByProperty("strategyId",strategyId);
        if(StringUtils.isNotEmpty(terminalId)) {
            return ExtResult.failResult(String.format("策略被使用:%s不能删除",terminalMapper.find(terminalId).getId()));
        }
        return ExtResult.successResult();
    }

    public List<TerminalStrategy> findByAgent(Integer agentId) {
        return terminalStrategyMapper.findByAgent(agentId);
    }
}
