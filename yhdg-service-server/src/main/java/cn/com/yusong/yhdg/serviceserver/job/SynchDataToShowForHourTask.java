package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.ChinaPostSynchDataToShowForHourService;
import cn.com.yusong.yhdg.serviceserver.service.basic.SynchDataToShowForHourService;

import java.io.IOException;

/**
 * 每小时
 */
public class SynchDataToShowForHourTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_SYNCH_DATA_TO_SHOW_FOR_HOUR_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
        SynchDataToShowForHourService synchDataToShowService = SpringContextHolder.getBean(SynchDataToShowForHourService.class);
        synchDataToShowService.transfer();
//        ChinaPostSynchDataToShowForHourService chinaPostSynchDataToShowService = SpringContextHolder.getBean(ChinaPostSynchDataToShowForHourService.class);
//        chinaPostSynchDataToShowService.transfer();
    }
}
