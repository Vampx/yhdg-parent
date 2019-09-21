package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AlipayfwPayOrderRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AlipayfwPayOrderRefundService {
    @Autowired
    AlipayfwPayOrderRefundMapper alipayfwPayOrderRefundMapper;

    public Page findPage(AlipayfwPayOrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayfwPayOrderRefundMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayfwPayOrderRefundMapper.findPageResult(search));
        return page;
    }
}
