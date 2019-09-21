package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryReportLogService;

import java.io.IOException;

/**
 * 十分钟
 */
public class TenMinuteTaskTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_TEN_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
    }
}
