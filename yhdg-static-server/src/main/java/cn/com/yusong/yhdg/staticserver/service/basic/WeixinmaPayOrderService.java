package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinmaPayOrder;
import cn.com.yusong.yhdg.staticserver.persistence.basic.WeixinmaPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WeixinmaPayOrderService extends PayOrderService {
    @Autowired
    WeixinmaPayOrderMapper weixinmaPayOrderMapper;

    public WeixinmaPayOrder find(String id) {
        return weixinmaPayOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int payOk(WeixinmaPayOrder order) {
        int effect = weixinmaPayOrderMapper.payOk(order.getId(),order.getPaymentId(), WeixinmaPayOrder.Status.INIT.getValue(), WeixinmaPayOrder.Status.SUCCESS.getValue(), new Date());
        if (effect > 0) {
        } else {
            log.warn("weixinmaPayOrderMapper.id is not exist, id: {}", order.getId());
        }
        return effect;
    }

}
