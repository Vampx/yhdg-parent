package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.staticserver.persistence.basic.WeixinPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WeixinPayOrderService extends PayOrderService {
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;

    public WeixinPayOrder find(String id) {
        return weixinPayOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int payOk(WeixinPayOrder order) {
        int effect = weixinPayOrderMapper.payOk(order.getId(),order.getPaymentId(), WeixinPayOrder.Status.INIT.getValue(), WeixinPayOrder.Status.SUCCESS.getValue(), new Date());
        if (effect > 0) {
        } else {
            log.warn("weixinPayOrderMapper.id is not exist, id: {}", order.getId());
        }
        return effect;
    }

}
