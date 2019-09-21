package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.LaxinRecordService;
import cn.com.yusong.yhdg.serviceserver.service.basic.WithdrawService;

public class WithdrawTransferTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_ONE_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() {
        WithdrawService withdrawService = SpringContextHolder.getBean(WithdrawService.class);
        withdrawService.transfer(null, null, null);
    }
}
