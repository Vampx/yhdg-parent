package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ActivityCustomer;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ActivityCustomerMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityCustomerService extends AbstractService {

    @Autowired
    ActivityCustomerMapper activityCustomerMapper;

    public Page findPage(ActivityCustomer search) {
        Page page = search.buildPage();
        page.setTotalItems(activityCustomerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<ActivityCustomer> list = activityCustomerMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }


}
