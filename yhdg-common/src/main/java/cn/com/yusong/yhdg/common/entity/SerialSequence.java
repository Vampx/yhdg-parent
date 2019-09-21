package cn.com.yusong.yhdg.common.entity;

import java.util.concurrent.locks.ReentrantLock;

public class SerialSequence {
    final ReentrantLock lock = new ReentrantLock();
    int num;
    long time;

    public int next() {
        lock.lock();

        try {
            long now = System.currentTimeMillis();
            if(time / 1000 != now / 1000) {
                time = now;
                num = 1;
            } else {
                ++num;
            }
        } finally {
            lock.unlock();
        }
        return num;
    }
}
