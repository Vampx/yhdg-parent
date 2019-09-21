package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmpPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class WeixinmpPayOrderService {
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;

    public WeixinmpPayOrder find(String id) {
        return weixinmpPayOrderMapper.find(id);
    }

    public WeixinmpPayOrder findBySourceId(String orderId) {
        return weixinmpPayOrderMapper.findBySourceId(orderId);
    }

    public WeixinmpPayOrder findBySourceIdeq(String sourceId) {
        return weixinmpPayOrderMapper.findBySourceIdeq(sourceId);
    }

    public Page findPage(WeixinmpPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(weixinmpPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(weixinmpPayOrderMapper.findPageResult(search));
        return page;
    }

    public List<WeixinmpPayOrder> findList(String mobile) {
        List<Integer> statusList = new ArrayList();
        statusList.add(WeixinmpPayOrder.Status.SUCCESS.getValue());
        statusList.add(WeixinmpPayOrder.Status.REFUND_SUCCESS.getValue());
        return weixinmpPayOrderMapper.findList(mobile, statusList);
    }

}
