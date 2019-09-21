package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AlipayPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chen on 2017/10/28.
 */
@Service
public class AlipayPayOrderService {
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;

    public AlipayPayOrder find(AlipayPayOrder search) {
        return alipayPayOrderMapper.find(search.getCustomerId(), search.getSourceType(), search.getSourceId(), search.getOrderStatus());
    }

    public Page findPage(AlipayPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayPayOrderMapper.findPageResult(search));
        return page;
    }

    public AlipayPayOrder findBySourceId(String orderId) {
        return alipayPayOrderMapper.findBySourceId(orderId);
    }


    public int insert(AlipayPayOrder alipayPayOrder) {
        return alipayPayOrderMapper.insert(alipayPayOrder);
    }
}
