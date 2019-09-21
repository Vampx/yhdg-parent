package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.serviceserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.serviceserver.service.zd.RentPeriodOrderService;

import java.io.IOException;

/**
 * 00:10
 */
public class ZeroTenTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_ZERO_TEN_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
        //租电包时段订单更新状态  一定要先更新过期状态，再更新使用状态
        RentPeriodOrderService rentPeriodOrderService = SpringContextHolder.getBean(RentPeriodOrderService.class);
        rentPeriodOrderService.expire();
        rentPeriodOrderService.used();

        //租车包时段订单更新状态
        VehiclePeriodOrderService vehiclePeriodOrderService = SpringContextHolder.getBean(VehiclePeriodOrderService.class);
        vehiclePeriodOrderService.expire();
        vehiclePeriodOrderService.used();

        CustomerCouponTicketService customerCouponTicketService = SpringContextHolder.getBean(CustomerCouponTicketService.class);
        customerCouponTicketService.wagesDay();
        BatteryService batteryService = SpringContextHolder.getBean(BatteryService.class);
        batteryService.batteryUseDay();
    }
}
