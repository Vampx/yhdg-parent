package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.LaxinRecordService;

public class LaxinRecordTransferTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_ONE_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() {
        LaxinRecordService laxinRecordService = SpringContextHolder.getBean(LaxinRecordService.class);
        laxinRecordService.transfer();
    }
}
