package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.CustomerCouponTicketService;

import java.io.IOException;

/**
 * 一小时
 */
public class OneHourTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_ONE_HOUR_TASK.getValue();
    }


    @Override
    protected void doBiz() throws IOException {
        CustomerCouponTicketService customerCouponTicketService = SpringContextHolder.getBean(CustomerCouponTicketService.class);
        customerCouponTicketService.expire();
    }
}
