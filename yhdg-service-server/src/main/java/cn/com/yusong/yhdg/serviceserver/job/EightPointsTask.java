package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.*;
import cn.com.yusong.yhdg.serviceserver.service.hdg.*;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 每天8点
 */
public class EightPointsTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_TWO_POINTS_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException, ParseException {

        CustomerInstallmentService customerInstallmentService = SpringContextHolder.getBean(CustomerInstallmentService.class);
        customerInstallmentService.willExpirePush();

        CustomerCouponTicketService customerCouponTicketService = SpringContextHolder.getBean(CustomerCouponTicketService.class);
        customerCouponTicketService.willExpirePush();

        BatteryService batteryService = SpringContextHolder.getBean(BatteryService.class);
        batteryService.unbindBatteryOutOverTime();
    }
}
