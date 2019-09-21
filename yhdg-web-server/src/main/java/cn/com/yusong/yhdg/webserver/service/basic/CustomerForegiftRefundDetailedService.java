package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftRefundDetailed;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerForegiftRefundDetailedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerForegiftRefundDetailedService {
    @Autowired
    CustomerForegiftRefundDetailedMapper customerForegiftRefundDetailedMapper;

    public Page findPage(CustomerForegiftRefundDetailed search) {
        Page page = search.buildPage();
        page.setTotalItems(customerForegiftRefundDetailedMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(customerForegiftRefundDetailedMapper.findPageResult(search));
        return page;
    }

    public CustomerForegiftRefundDetailed find(String id,int num) {
        return customerForegiftRefundDetailedMapper.find(id,num);
    }


}
