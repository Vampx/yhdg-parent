package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmpPayOrderRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WeixinmpPayOrderRefundService {
    @Autowired
    WeixinmpPayOrderRefundMapper weixinmpPayOrderRefundMapper;

    public Page findPage(WeixinmpPayOrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmpPayOrderRefundMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(weixinmpPayOrderRefundMapper.findPageResult(search));
        return page;
    }

}
