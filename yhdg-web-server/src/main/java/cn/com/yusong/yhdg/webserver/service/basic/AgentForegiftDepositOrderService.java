package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentForegiftDepositOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentForegiftDepositOrderService extends AbstractService{

    @Autowired
    AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;

    public Page findPage(AgentForegiftDepositOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(agentForegiftDepositOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentForegiftDepositOrder> pageResult = agentForegiftDepositOrderMapper.findPageResult(search);
        page.setResult(pageResult);
        return page;
    }
}
