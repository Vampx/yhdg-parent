package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerNoticeMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerNoticeMessageService {
    @Autowired
    CustomerNoticeMessageMapper customerNoticeMessageMapper;

    public CustomerNoticeMessage find(long id) {
        return customerNoticeMessageMapper.find(id);
    }

    public Page findPage(CustomerNoticeMessage search) {
        Page page = search.buildPage();
        page.setTotalItems(customerNoticeMessageMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(customerNoticeMessageMapper.findPageResult(search));
        return page;
    }

}
