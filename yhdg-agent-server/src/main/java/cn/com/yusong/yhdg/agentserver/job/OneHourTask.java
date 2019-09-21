package cn.com.yusong.yhdg.agentserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;

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
//        AccreditService accreditService = SpringContextHolder.getBean(AccreditService.class);
//        accreditService.getAccreditTime();
    }
}
