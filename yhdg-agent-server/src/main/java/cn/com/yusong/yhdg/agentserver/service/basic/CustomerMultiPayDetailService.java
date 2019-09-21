package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMultiPayDetailMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiPayDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMultiPayDetailService extends AbstractService {

    @Autowired
    CustomerMultiPayDetailMapper customerMultiPayDetailMapper;

    public Page findPage(CustomerMultiPayDetail search) {
        Page page = search.buildPage();
        page.setTotalItems(customerMultiPayDetailMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerMultiPayDetail> list = customerMultiPayDetailMapper.findPageResult(search);
        page.setResult(list);
        return page;
    }

}
