package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryOrderService;

import java.io.IOException;

/**
 * 每天23点
 */
public class TwentyThreePointsTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_TWENTY_THREE_POINTS_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
        BatteryOrderService batteryOrderService = SpringContextHolder.getBean(BatteryOrderService.class);
        batteryOrderService.moveHistory();

    }
}
