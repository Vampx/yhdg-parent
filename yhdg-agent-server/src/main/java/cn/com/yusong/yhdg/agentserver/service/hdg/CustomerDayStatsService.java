package cn.com.yusong.yhdg.agentserver.service.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.CustomerDayStats;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.CustomerDayStatsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDayStatsService {
    @Autowired
    CustomerDayStatsMapper customerDayStatsMapper;

    public Page findPage(CustomerDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(customerDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(customerDayStatsMapper.findPageResult(search));
        return page;
    }

    public List<CustomerDayStats> findForExcel (CustomerDayStats search) {
        return customerDayStatsMapper.findPageResult(search);
    }
}
