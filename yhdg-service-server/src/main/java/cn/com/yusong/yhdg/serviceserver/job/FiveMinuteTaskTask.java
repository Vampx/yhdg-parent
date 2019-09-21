package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.hdg.ZhiZuPushMessageService;

import java.io.IOException;

/**
 * 五分钟
 */
public class FiveMinuteTaskTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_FIVE_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
        ZhiZuPushMessageService zhiZuPushMessageService = SpringContextHolder.getBean(ZhiZuPushMessageService.class);
        zhiZuPushMessageService.pushBatteryInfo();
        zhiZuPushMessageService.pushCabinetDayDegree();
    }
}
