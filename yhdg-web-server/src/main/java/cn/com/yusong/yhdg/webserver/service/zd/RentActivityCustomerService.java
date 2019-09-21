package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentActivityCustomer;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentActivityCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentActivityCustomerService {
    @Autowired
    private RentActivityCustomerMapper rentActivityCustomerMapper;

    public Page findPage(RentActivityCustomer search) {
        Page page = search.buildPage();
        page.setTotalItems(rentActivityCustomerMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(rentActivityCustomerMapper.findPageResult(search));
        return page;
    }

}
