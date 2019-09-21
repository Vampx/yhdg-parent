package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.ChinaPostSynchDataToShowForMinuteService;
import cn.com.yusong.yhdg.serviceserver.service.basic.SynchDataToShowForMinuteService;

import java.io.IOException;

/**
 * 每5分钟
 */
public class SynchDataToShowForMinuteTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_SYNCH_DATA_TO_SHOW_FOR_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException {
        SynchDataToShowForMinuteService synchDataToShowService = SpringContextHolder.getBean(SynchDataToShowForMinuteService.class);
        synchDataToShowService.transfer();
//        ChinaPostSynchDataToShowForMinuteService chinaPostSynchDataToShowService = SpringContextHolder.getBean(ChinaPostSynchDataToShowForMinuteService.class);
//        chinaPostSynchDataToShowService.transfer();
    }
}
