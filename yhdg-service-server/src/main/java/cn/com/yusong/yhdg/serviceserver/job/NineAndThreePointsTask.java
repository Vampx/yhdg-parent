package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.serviceserver.service.basic.CustomerInstallmentService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.PacketPeriodOrderService;

import java.io.IOException;
import java.text.ParseException;

/**
 * 每天8点下午3点
 */
public class NineAndThreePointsTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_TWO_POINTS_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException, ParseException {
        PacketPeriodOrderService packetPeriodOrderService = SpringContextHolder.getBean(PacketPeriodOrderService.class);
        packetPeriodOrderService.willExpirePush();

        BatteryOrderService batteryOrderService = SpringContextHolder.getBean(BatteryOrderService.class);
        batteryOrderService.batteryOrderTakeTimeOut();

        BatteryService batteryService = SpringContextHolder.getBean(BatteryService.class);
        batteryService.unbindBatteryOutOverTime();
    }
}
