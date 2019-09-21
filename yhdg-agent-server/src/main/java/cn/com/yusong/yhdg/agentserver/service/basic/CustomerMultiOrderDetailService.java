package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMultiOrderDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMultiOrderDetailService extends AbstractService {

    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;

    public Page findPage(CustomerMultiOrderDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(customerMultiOrderDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerMultiOrderDetail> list = customerMultiOrderDetailMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

}
