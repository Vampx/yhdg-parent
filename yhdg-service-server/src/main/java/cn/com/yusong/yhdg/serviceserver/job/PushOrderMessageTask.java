package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.hdg.ZhiZuPushMessageService;

/**
 * Created by ruanjian5 on 2017/12/12.
 */
public class PushOrderMessageTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NOED_PUSH_ORDER_MESSAGE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
        ZhiZuPushMessageService zhiZuPushMessageService = SpringContextHolder.getBean(ZhiZuPushMessageService.class);
        zhiZuPushMessageService.scanMessage();
    }
}
