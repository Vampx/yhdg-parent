package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.BatteryOrderRefundMapper;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruanjian5 on 2017/11/8.
 */
@Service
public class BatteryOrderRefundSerService   {
    @Autowired
    BatteryOrderRefundMapper batteryOrderRefundMapper;
    public int insert(BatteryOrderRefund batteryOrder){
        return batteryOrderRefundMapper.insert(batteryOrder);
    }
}
