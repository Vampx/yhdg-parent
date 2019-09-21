package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.AgentDayInOutMoneyService;
import cn.com.yusong.yhdg.serviceserver.service.basic.AgentPayService;
import cn.com.yusong.yhdg.serviceserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.serviceserver.service.basic.PartnerInOutCashService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.*;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 每天凌晨2点
 */
public class TwoPointsTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_TWO_POINTS_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException, ParseException {
        OrderIdService orderIdService = SpringContextHolder.getBean(OrderIdService.class);
        orderIdService.clean();

        BatteryReportLogService batteryReportLogService = SpringContextHolder.getBean(BatteryReportLogService.class);
        batteryReportLogService.create();

        AgentPayService agentPayService = SpringContextHolder.getBean(AgentPayService.class);
        agentPayService.stats(DateUtils.addDays(new Date(), -1));

        IncomeStatsService incomeStatsService = SpringContextHolder.getBean(IncomeStatsService.class);
        incomeStatsService.stats(DateUtils.addDays(new Date(), -1), true);

        AgentDayInOutMoneyService agentDayInOutMoneyService = SpringContextHolder.getBean(AgentDayInOutMoneyService.class);
        agentDayInOutMoneyService.stats(DateUtils.addDays(new Date(), -1));

        PartnerInOutCashService  partnerInOutCashService = SpringContextHolder.getBean(PartnerInOutCashService.class);
        partnerInOutCashService.stats(DateUtils.addDays(new Date(), -1));

        CabinetService cabinetService = SpringContextHolder.getBean(CabinetService.class);
        cabinetService.SyncVolume();
    }
}
