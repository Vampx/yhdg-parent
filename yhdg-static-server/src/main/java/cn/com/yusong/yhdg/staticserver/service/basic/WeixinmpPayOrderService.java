package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.staticserver.persistence.basic.WeixinPayOrderMapper;
import cn.com.yusong.yhdg.staticserver.persistence.basic.WeixinmpPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WeixinmpPayOrderService extends PayOrderService {
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;

    public WeixinmpPayOrder find(String id) {
        return weixinmpPayOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int payOk(WeixinmpPayOrder order) {
        int effect = weixinmpPayOrderMapper.payOk(order.getId(),order.getPaymentId(), WeixinmpPayOrder.Status.INIT.getValue(), WeixinmpPayOrder.Status.SUCCESS.getValue(), new Date());
        if (effect > 0) {
        } else {
            log.warn("weixinmpPayOrderMapper.id is not exist, id: {}", order.getId());
        }
        return effect;
    }

}
