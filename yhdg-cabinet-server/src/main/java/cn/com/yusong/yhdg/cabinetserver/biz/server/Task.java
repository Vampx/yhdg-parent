package cn.com.yusong.yhdg.cabinetserver.biz.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Task implements Runnable {

    private final Logger log = LogManager.getLogger(Task.class);

    public long submitTime = 0;
    public long beginTime = 0;
    public long endTime = 0;

    Runnable task;
    String tag;

    public Task(Runnable task, String tag) {
        this.task = task;
        this.tag = tag;
        submitTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        before();
        try {
            task.run();
        } catch (Throwable e) {
            log.error("Task execute error", e);
            e.printStackTrace();
        } finally {
            after();
        }
    }

    protected void before() {
        beginTime = System.currentTimeMillis();
        if (log.isInfoEnabled()) {
            log.info("{} {} begin, submit: {}ms", tag, task, beginTime - submitTime);
        }
    }

    protected void after() {
        endTime = System.currentTimeMillis();

        if (log.isInfoEnabled()) {
            log.info("{} {} end", tag, task);
            log.info("{} {} submit: {}ms, run:{}ms", tag, task, beginTime - submitTime, endTime - beginTime);
        }

    }
}
