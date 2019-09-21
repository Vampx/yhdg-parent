package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmaPayOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WeixinmaPayOrderService extends AbstractService {
    
    @Autowired
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;

    public WeixinmaPayOrder find(String id) {
        return weixinmaPayOrderMapper.find(id);
    }

    public WeixinmaPayOrder findBySourceId(String orderId) {
        return weixinmaPayOrderMapper.findBySourceId(orderId);
    }

    public WeixinmaPayOrder findBySourceIdeq(String sourceId) {
        return weixinmaPayOrderMapper.findBySourceIdeq(sourceId);
    }

    public Page findPage(WeixinmaPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmaPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(weixinmaPayOrderMapper.findPageResult(search));
        return page;
    }

    public List<WeixinmaPayOrder> findList(String mobile) {
        List<Integer> statusList = new ArrayList();
        statusList.add(WeixinmaPayOrder.Status.SUCCESS.getValue());
        statusList.add(WeixinmaPayOrder.Status.REFUND_SUCCESS.getValue());
        return weixinmaPayOrderMapper.findList(mobile, statusList);
    }

}
