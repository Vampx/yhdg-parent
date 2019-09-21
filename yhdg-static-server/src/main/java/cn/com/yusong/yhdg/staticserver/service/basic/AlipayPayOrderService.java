package cn.com.yusong.yhdg.staticserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.PayOrder;
import cn.com.yusong.yhdg.staticserver.persistence.basic.AlipayPayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AlipayPayOrderService extends PayOrderService {
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;

    public AlipayPayOrder find(String id) {
        return alipayPayOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public int payOk(AlipayPayOrder order) {
        int effect = alipayPayOrderMapper.payOk(order.getId(),order.getPaymentId(), AlipayPayOrder.Status.INIT.getValue(), AlipayPayOrder.Status.SUCCESS.getValue(), new Date());
        if (effect > 0) {
        } else {
            log.warn("alipayPayOrder.id is not exist, id: {}", order.getId());
        }
        return effect;
    }

}
