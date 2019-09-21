package cn.com.yusong.yhdg.serviceserver.job;


import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.service.basic.AgentDayInOutMoneyService;
import cn.com.yusong.yhdg.serviceserver.service.basic.AgentPayService;
import cn.com.yusong.yhdg.serviceserver.service.basic.PartnerInOutCashService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.BatteryReportLogService;
import cn.com.yusong.yhdg.serviceserver.service.hdg.IncomeStatsService;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每小时的15分钟
 */
public class HourlyFifteenMinutesTask extends ConcurrentTask {
    @Override
    protected String getZkNode() {
        return ConstEnum.Node.NODE_HOURLY_FIFTEEN_MINUTE_TASK.getValue();
    }

    @Override
    protected void doBiz() throws IOException, ParseException {
        IncomeStatsService incomeStatsService = SpringContextHolder.getBean(IncomeStatsService.class);
     incomeStatsService.stats(new Date(), false);



/*


        Date date = null;
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );

        PartnerInOutCashService partnerInOutCashService = SpringContextHolder.getBean(PartnerInOutCashService.class);
        date = sdf.parse("2019-05-01");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-02");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-03");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-04");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-05");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-06");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-07");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-08");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-09");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-10");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-11");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-12");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-13");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-14");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-15");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-16");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-17");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-18");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-19");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-20");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-21");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-22");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-23");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-24");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-25");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-26");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-27");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-28");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-29");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-30");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-05-31");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-01");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-02");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-03");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-04");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-05");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-06");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-07");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-08");
        partnerInOutCashService.stats(date);
        date = sdf.parse("2019-06-09");
        partnerInOutCashService.stats(date);


        AgentDayInOutMoneyService agentDayInOutMoneyService = SpringContextHolder.getBean(AgentDayInOutMoneyService.class);
        date = sdf.parse("2019-05-01");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-02");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-03");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-04");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-05");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-06");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-07");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-08");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-09");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-10");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-11");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-12");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-13");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-14");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-15");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-16");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-17");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-18");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-19");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-20");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-21");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-22");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-23");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-24");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-25");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-26");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-27");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-28");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-29");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-30");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-05-31");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-01");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-02");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-03");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-04");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-05");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-06");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-07");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-08");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-09");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-10");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-11");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-12");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-13");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-14");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-15");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-16");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-17");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-18");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-19");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-20");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-21");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-22");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-23");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-24");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-25");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-26");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-27");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-28");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-29");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-06-30");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-01");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-02");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-03");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-04");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-05");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-06");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-07");
        agentDayInOutMoneyService.stats(date);
        date = sdf.parse("2019-07-08");
        agentDayInOutMoneyService.stats(date);


        AgentPayService agentPayService = SpringContextHolder.getBean(AgentPayService.class);
        date = sdf.parse("2019-05-01");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-02");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-03");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-04");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-05");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-06");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-07");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-08");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-09");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-10");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-11");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-12");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-13");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-14");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-15");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-16");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-17");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-18");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-19");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-20");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-21");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-22");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-23");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-24");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-25");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-26");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-27");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-28");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-29");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-30");
        agentPayService.stats(date);
        date = sdf.parse("2019-05-31");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-01");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-02");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-03");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-04");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-05");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-06");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-07");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-08");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-09");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-10");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-11");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-12");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-13");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-14");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-15");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-16");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-17");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-18");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-19");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-20");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-21");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-22");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-23");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-24");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-25");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-26");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-27");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-28");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-29");
        agentPayService.stats(date);
        date = sdf.parse("2019-06-30");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-01");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-02");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-03");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-04");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-05");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-06");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-07");
        agentPayService.stats(date);
        date = sdf.parse("2019-07-08");
        agentPayService.stats(date);





        date = sdf.parse("2019-05-03");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-04");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-05");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-06");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-07");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-08");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-09");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-10");
        incomeStatsService.stats(date, true);


        date = sdf.parse("2019-05-11");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-12");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-13");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-14");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-15");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-16");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-17");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-18");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-19");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-20");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-21");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-22");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-23");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-24");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-25");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-26");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-27");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-28");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-05-29");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-30");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-05-31");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-01");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-02");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-03");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-04");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-05");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-06");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-07");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-08");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-09");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-10");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-11");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-12");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-13");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-14");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-15");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-16");
        incomeStatsService.stats(date, true);

       date = sdf.parse("2019-06-17");
       incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-18");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-19");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-20");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-21");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-22");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-23");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-24");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-25");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-26");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-27");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-06-28");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-29");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-06-30");
        incomeStatsService.stats(date, true);
        date = sdf.parse("2019-07-01");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-02");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-03");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-04");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-05");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-06");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-07");
        incomeStatsService.stats(date, true);

        date = sdf.parse("2019-07-08");
        incomeStatsService.stats(date, true);

*/

    }
}
