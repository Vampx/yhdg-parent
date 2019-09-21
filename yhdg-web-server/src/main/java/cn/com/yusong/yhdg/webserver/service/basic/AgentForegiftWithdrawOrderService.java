package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftWithdrawOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentForegiftWithdrawOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentForegiftWithdrawOrderService extends AbstractService{

    @Autowired
    AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;

    public Page findPage(AgentForegiftWithdrawOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(agentForegiftWithdrawOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentForegiftWithdrawOrder> pageResult = agentForegiftWithdrawOrderMapper.findPageResult(search);
        page.setResult(pageResult);
        return page;
    }
}
