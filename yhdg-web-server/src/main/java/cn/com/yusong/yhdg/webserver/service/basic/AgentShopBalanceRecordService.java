package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecord;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentShopBalanceRecordMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentShopBalanceRecordService extends AbstractService{

    @Autowired
    AgentShopBalanceRecordMapper agentShopBalanceRecordMapper;

    public Page findPage(AgentShopBalanceRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(agentShopBalanceRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentShopBalanceRecord> pageResult = agentShopBalanceRecordMapper.findPageResult(search);
        page.setResult(pageResult);
        return page;
    }
}
