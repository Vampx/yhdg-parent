package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AlipayfwPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlipayfwPayOrderService {
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;

    public AlipayfwPayOrder find(String id) {
        return alipayfwPayOrderMapper.find(id);
    }

    public AlipayfwPayOrder findBySourceId(String orderId) {
        return alipayfwPayOrderMapper.findBySourceId(orderId);
    }

    public AlipayfwPayOrder findBySourceIdeq(String sourceId) {
        return alipayfwPayOrderMapper.findBySourceIdeq(sourceId);
    }

    public List<AlipayfwPayOrder> findList(String mobile) {
        List<Integer> statusList = new ArrayList();
        statusList.add(AlipayfwPayOrder.Status.SUCCESS.getValue());
        statusList.add(AlipayfwPayOrder.Status.REFUND_SUCCESS.getValue());
        return alipayfwPayOrderMapper.findList(mobile, statusList);
    }

    public Page findPage(AlipayfwPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayfwPayOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayfwPayOrderMapper.findPageResult(search));
        return page;
    }

    public Page findPageByPacketRefund(AlipayfwPayOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(alipayfwPayOrderMapper.findPageByPacketRefundCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(alipayfwPayOrderMapper.findPageByPacketRefundResult(search));
        return page;
    }

}
