package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinPayOrderRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WeixinPayOrderRefundService {
    @Autowired
    WeixinPayOrderRefundMapper WeixinPayOrderRefundMapper;

    public Page findPage(WeixinPayOrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(WeixinPayOrderRefundMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(WeixinPayOrderRefundMapper.findPageResult(search));
        return page;
    }

}
