package cn.com.yusong.yhdg.serviceserver.job;

import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.Date;

public class JobManager {
    protected static final Logger log = LogManager.getLogger(JobManager.class);

    Scheduler scheduler;

    public JobManager(AppConfig config) throws SchedulerException {
        scheduler = new StdSchedulerFactory().getScheduler();
        config.jobManager = this;
    }

    public void start() throws SchedulerException, ParseException {
        try {
            twoPointsTask(scheduler);
            eightPointsTask(scheduler);
            nineAndThreePointsTask(scheduler);
            fiveMinuteTask(scheduler);
            oneMinuteTask(scheduler);
            oneHourTask(scheduler);
            tenMinuteTask(scheduler);
            twentyThreePointsTask(scheduler);
            zeroTenTask(scheduler);
            pushMessageTask(scheduler);
            pushOrderMessageTask(scheduler);
            //weixinmpTransferTask(scheduler);
            //syncCustomerInfoTask(scheduler);
            hourlyFifteenMinutesTask(scheduler);
            laxinRecordTransferTask(scheduler);
            withdrawTransferTask(scheduler);
//            synchDataToShowForMinuteTask(scheduler);
//            synchDataToShowForHourTask(scheduler);
            customerOfflineExchangeTask(scheduler);
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("start job error", e);
        }
    }

    public void stop() {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
                scheduler = null;
            }
        } catch (SchedulerException e) {
            log.error("stop job error", e);
        }
    }

    //每天凌晨2点
    void twoPointsTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("two_points_task_job", "task_group", TwoPointsTask.class);
        CronTrigger cronTrigger = new CronTrigger("two_points_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0 2 * * ?"));  //每天凌晨2点, 触发一次
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //每天凌晨8点
    void eightPointsTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("eight_points_task_job", "task_group", EightPointsTask.class);
        CronTrigger cronTrigger = new CronTrigger("eight_points_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0 8 * * ?"));  //每天凌晨8点, 触发一次
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //每天9点半和下午3电半
    void nineAndThreePointsTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("nine_and_three_points_task_job", "task_group", NineAndThreePointsTask.class);
        CronTrigger cronTrigger = new CronTrigger("nine_and_three_points_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 30 9,15 * * ?"));

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    //每天23点
    void twentyThreePointsTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("twenty_three_points_task_job", "task_group", TwentyThreePointsTask.class);
        CronTrigger cronTrigger = new CronTrigger("twenty_three_points_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0 23 * * ?"));  //每天23点, 触发一次
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //每天00:10
    void zeroTenTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("zero_ten_task_job", "task_group",ZeroTenTask.class);
        CronTrigger cronTrigger = new CronTrigger("zero_ten_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 10 0 * * ?"));  //每天00:10, 触发一次
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    void laxinRecordTransferTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("laxin_record_transfer_task_job", "task_group",LaxinRecordTransferTask.class);
        CronTrigger cronTrigger = new CronTrigger("laxin_record_transfer_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0/10 * * * ?"));
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    void withdrawTransferTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("withdraw_transfer_task_job", "task_group",WithdrawTransferTask.class);
        CronTrigger cronTrigger = new CronTrigger("withdraw_transfer_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0/10 * * * ?"));
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //十分钟
    void tenMinuteTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("ten_minute_task_job", "task_group", TenMinuteTaskTask.class);
        SimpleTrigger trigger = new SimpleTrigger("ten_minute_online_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 * 60 * 10); //10分钟
        scheduler.scheduleJob(jobDetail, trigger);
    }
    //五分钟
    void fiveMinuteTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("five_minute_task_job", "task_group", FiveMinuteTaskTask.class);
        SimpleTrigger trigger = new SimpleTrigger("five_minute_online_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 * 60 * 5); //5分钟
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //一分钟
    void oneMinuteTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("one_minute_task_job", "task_groub", OneMinuteTask.class);
        SimpleTrigger trigger = new SimpleTrigger("one_minute_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 * 60 * 1); //1分钟
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //一小时
    void oneHourTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("one_hour_task_job", "task_groub", OneHourTask.class);
        SimpleTrigger trigger = new SimpleTrigger("one_hour_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 * 60 * 60); //1小时
        scheduler.scheduleJob(jobDetail, trigger);
    }

    void pushMessageTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("push_message_task_job", "task_groub", PushMessageTask.class);
        SimpleTrigger trigger = new SimpleTrigger("push_message_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 ); //1s
        scheduler.scheduleJob(jobDetail, trigger);
    }

    void customerOfflineExchangeTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("customre_offline_exchange_task_job", "task_groub", CustomerOfflineExchangeTask.class);
        SimpleTrigger trigger = new SimpleTrigger("customre_offline_exchange_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                1000 ); //1s
        scheduler.scheduleJob(jobDetail, trigger);
    }

    void pushOrderMessageTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("push_order_message_task_job", "task_group", PushOrderMessageTask.class);
        SimpleTrigger trigger = new SimpleTrigger("push_order_message_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                2000 ); //1s
        scheduler.scheduleJob(jobDetail, trigger);
    }

    void syncCustomerInfoTask(Scheduler scheduler) throws ParseException, SchedulerException {
        JobDetail jobDetail = new JobDetail("sync_customer_info_task_job", "task_group", SyncCustomerInfoTask.class);
        SimpleTrigger trigger = new SimpleTrigger("sync_customer_info_task_trigger",
                "job_group",
                new Date(),
                null,
                SimpleTrigger.REPEAT_INDEFINITELY,
                2000 ); //1s
        scheduler.scheduleJob(jobDetail, trigger);
    }

    //每小时的15分钟
    void hourlyFifteenMinutesTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("hourly_fifteen_minutes_task_job", "task_group", HourlyFifteenMinutesTask.class);
        CronTrigger cronTrigger = new CronTrigger("hourly_fifteen_minutes_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 15 * * * ?"));
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //每5分钟
    void synchDataToShowForMinuteTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("synch_data_to_show_for_minute_task_job", "task_group", SynchDataToShowForMinuteTask.class);
        CronTrigger cronTrigger = new CronTrigger("synch_data_to_show_for_minute_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 0/5 * * * ?"));
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    //每天2点
    void synchDataToShowForHourTask(Scheduler scheduler) throws SchedulerException, ParseException {
        JobDetail jobDetail = new JobDetail("synch_data_to_show_for_hour_task_job", "task_group", SynchDataToShowForHourTask.class);
        CronTrigger cronTrigger = new CronTrigger("synch_data_to_show_for_hour_task_trigger", "task_group");
        cronTrigger.setCronExpression(new CronExpression("0 30 0/2 * * ?"));
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}
