package cn.com.yusong.yhdg.serviceserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.BackBatteryOrderMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.BatteryMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.CabinetBoxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BackBatteryOrderService {
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;

    public void refreshExpireOrder() {
        List<BackBatteryOrder> orderList = backBatteryOrderMapper.findStatus(new Date(), BackBatteryOrder.OrderStatus.SUCCESS.getValue());
        for(BackBatteryOrder order : orderList) {
            expireOrder(order);
        }
    }

    @Transactional(rollbackFor=Throwable.class)
    public void expireOrder(BackBatteryOrder order) {
        if(backBatteryOrderMapper.updateStatus(order.getId(), BackBatteryOrder.OrderStatus.SUCCESS.getValue(), BackBatteryOrder.OrderStatus.EXPIRE.getValue()) == 1) {
            cabinetBoxMapper.updateStatus(order.getCabinetId(), order.getBoxNum(), CabinetBox.BoxStatus.BACK_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
            //customerMapper.updateBackBatteryOrderId(order.getCustomerId(), null);
        }
    }
}
