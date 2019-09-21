package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerBalanceDeduct;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerBalanceDeductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerBalanceDeductService {
    @Autowired
    CustomerBalanceDeductMapper customerBalanceDeductMapper;

    public CustomerBalanceDeduct find(int id) {
        return customerBalanceDeductMapper.find(id);
    }

    public Page findPage(CustomerBalanceDeduct search) {
        Page page = search.buildPage();
        page.setTotalItems(customerBalanceDeductMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(customerBalanceDeductMapper.findPageResult(search));
        return page;
    }

}
