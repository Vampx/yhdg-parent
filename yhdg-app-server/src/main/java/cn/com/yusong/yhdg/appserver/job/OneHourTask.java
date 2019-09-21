package cn.com.yusong.yhdg.appserver.job;


import cn.com.yusong.yhdg.appserver.service.hdg.AccreditService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;

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
