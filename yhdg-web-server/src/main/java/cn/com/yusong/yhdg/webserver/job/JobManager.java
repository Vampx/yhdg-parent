package cn.com.yusong.yhdg.webserver.job;

import cn.com.yusong.yhdg.webserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
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
            oneHourTask(scheduler);
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
}
