package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.hdg.SyncCustomerInfoService;

/**
 * Created by ruanjian5 on 2017/12/12.
 */
public class SyncCustomerInfoTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.SYNC_CUSTOMER_INFO_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
        SyncCustomerInfoService syncCustomerInfoService = SpringContextHolder.getBean(SyncCustomerInfoService.class);
        syncCustomerInfoService.scanMessage();
    }
}
