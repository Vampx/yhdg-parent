package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwPayOrder;
import cn.com.yusong.yhdg.staticserver.persistence.basic.AlipayfwPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AlipayfwPayOrderService extends PayOrderService {
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;

    public AlipayfwPayOrder find(String id) {
        return alipayfwPayOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int payOk(AlipayfwPayOrder order) {
        int effect = alipayfwPayOrderMapper.payOk(order.getId(),order.getPaymentId(), AlipayfwPayOrder.Status.INIT.getValue(), AlipayfwPayOrder.Status.SUCCESS.getValue(), new Date());
        if (effect > 0) {
        } else {
            log.warn("alipayfwPayOrderMapper.id is not exist, id: {}", order.getId());
        }
        return effect;
    }

}
