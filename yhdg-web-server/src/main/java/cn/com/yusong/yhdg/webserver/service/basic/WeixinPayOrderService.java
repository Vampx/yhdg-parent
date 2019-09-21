package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chen on 2017/10/28.
 */
@Service
public class WeixinPayOrderService {
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;

    public WeixinPayOrder find(WeixinPayOrder search) {
        return weixinPayOrderMapper.find(search.getCustomerId(), search.getSourceType(), search.getSourceId(), search.getOrderStatus());
    }

    public WeixinPayOrder findBySourceId(String orderId) {
        return weixinPayOrderMapper.findBySourceId(orderId);
    }

    public Page findPage(WeixinPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(weixinPayOrderMapper.findPageResult(search));
        return page;
    }

}
