package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecordDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentShopBalanceRecordDetailMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentShopBalanceRecordDetailService extends AbstractService{

    @Autowired
    AgentShopBalanceRecordDetailMapper agentShopBalanceRecordDetailMapper;

    public Page findPage(AgentShopBalanceRecordDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(agentShopBalanceRecordDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentShopBalanceRecordDetail> pageResult = agentShopBalanceRecordDetailMapper.findPageResult(search);
        page.setResult(pageResult);
        return page;
    }

}
