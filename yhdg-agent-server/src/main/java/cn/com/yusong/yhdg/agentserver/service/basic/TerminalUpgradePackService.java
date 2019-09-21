package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.TerminalUpgradePackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
public class TerminalUpgradePackService {

    @Autowired
    TerminalUpgradePackMapper terminalUpgradePackMapper;

    @Autowired
    AppConfig appConfig;

    public TerminalUpgradePack find(long id){return terminalUpgradePackMapper.find(id);}
    public Page findPage(TerminalUpgradePack search){
        Page page = search.buildPage();
        page.setTotalItems(terminalUpgradePackMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(terminalUpgradePackMapper.findPageResult(search));
        return page;
    }

    public ExtResult insert(TerminalUpgradePack entity) {
        entity.setUpdateTime(new Date());
        terminalUpgradePackMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor=Throwable.class)
    public ExtResult update(TerminalUpgradePack entity) throws IOException {
        TerminalUpgradePack upgradePack = terminalUpgradePackMapper.find(entity.getId());
        if(upgradePack == null) {
            return ExtResult.failResult("记录不存在");
        }

        entity.setUpdateTime(new Date());
        terminalUpgradePackMapper.update(entity);

        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        if (terminalUpgradePackMapper.delete(id) == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }
}
