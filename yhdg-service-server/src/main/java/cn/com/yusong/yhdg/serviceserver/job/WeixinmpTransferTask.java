package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.service.hdg.DayBalanceRecordService;
import org.quartz.StatefulJob;

import java.io.File;

/**
 * 微信公众号转账任务
 */
public class WeixinmpTransferTask extends ConcurrentTask implements StatefulJob {

    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_WEIXINMP_TRANSFER_TASK.getValue();
    }

    @Override
    protected void doBiz() throws Exception {
//        DayBalanceRecordService dayBalanceRecordService = SpringContextHolder.getBean(DayBalanceRecordService.class);
//        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
//
//        dayBalanceRecordService.stats();
//        dayBalanceRecordService.transferToWeixinMp(null, new File(config.appDir, "/WEB-INF/apiclient_cert.p12"), config.getWxappAppId(), config.getWxappPartnerId(), config.getWxappPartnerKey());
    }
}
