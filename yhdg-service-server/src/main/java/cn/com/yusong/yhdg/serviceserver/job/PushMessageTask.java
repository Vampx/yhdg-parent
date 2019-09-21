package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.PushConfig;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.push.PushConfigFactory;
import cn.com.yusong.yhdg.serviceserver.service.basic.PushMessageService;

/**
 * Created by ruanjian5 on 2017/12/12.
 */
public class PushMessageTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NOED_PUSH_MESSAGE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
        PushMessageService pushMessageService = SpringContextHolder.getBean(PushMessageService.class);
        pushMessageService.scanMessage();
    }
}
