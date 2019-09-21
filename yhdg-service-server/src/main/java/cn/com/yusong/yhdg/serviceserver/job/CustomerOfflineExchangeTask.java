package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.CustomerOfflineExchangeService;
import cn.com.yusong.yhdg.serviceserver.service.basic.PushMessageService;

/**
 * Created by ruanjian5 on 2017/12/12.
 */
public class CustomerOfflineExchangeTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NOED_CUSTOMRR_OFFLINE_EXCHANGE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
        CustomerOfflineExchangeService customerOfflineExchangeService = SpringContextHolder.getBean(CustomerOfflineExchangeService.class);
        customerOfflineExchangeService.dealOfflineBattery();
        customerOfflineExchangeService.dealOfflineExchangeRecord();
    }
}
