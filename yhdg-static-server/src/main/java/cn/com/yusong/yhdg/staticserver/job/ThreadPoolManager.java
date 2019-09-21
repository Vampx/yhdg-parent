package cn.com.yusong.yhdg.staticserver.job;


import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolManager {
    ExecutorService videoThreadPool = Executors.newFixedThreadPool(1);
    ExecutorService publishThreadPool = Executors.newFixedThreadPool(1);
    ExecutorService playbillPackThreadPool = Executors.newFixedThreadPool(2);
    ExecutorService programPackThreadPool = Executors.newFixedThreadPool(2);
    ExecutorService pickDataThreadPool = Executors.newFixedThreadPool(5);
    ExecutorService materialPackThreadPool = Executors.newFixedThreadPool(2);

    public void addVideo(Runnable runnable) {
        videoThreadPool.submit(runnable);
    }

    public void addPublish(Runnable runnable) {
        publishThreadPool.submit(runnable);
    }

    public void addPlaybillPack(Runnable runnable) {
        playbillPackThreadPool.submit(runnable);
    }

    public void addProgramPack(Runnable runnable) {
        programPackThreadPool.submit(runnable);
    }

    public void addPickData(Runnable runnable) {
        pickDataThreadPool.submit(runnable);
    }

    public void addMaterialPack(Runnable runnable) {
        materialPackThreadPool.submit(runnable);
    }
}