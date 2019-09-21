package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrderDetail;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMultiOrderDetailMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
