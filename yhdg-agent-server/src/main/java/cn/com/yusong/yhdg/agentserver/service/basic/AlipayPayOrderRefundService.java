package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrderRefund;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AlipayPayOrderRefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AlipayPayOrderRefundService {
    @Autowired
    AlipayPayOrderRefundMapper alipayPayOrderRefundMapper;

    public Page findPage(AlipayPayOrderRefund search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayPayOrderRefundMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayPayOrderRefundMapper.findPageResult(search));
        return page;
    }
}
