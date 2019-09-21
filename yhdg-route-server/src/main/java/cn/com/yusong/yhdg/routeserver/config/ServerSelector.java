package cn.com.yusong.yhdg.routeserver.config;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerSelector {

    static final Logger log = LogManager.getLogger(ServerSelector.class);

    ServerInfo[] serverList;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    public void update(List<ServerInfo> list) {
        if(log.isDebugEnabled()) {
            log.debug("server list change: count: {}, {}", list.size(), StringUtils.join(list, ", "));
        }

        writeLock.lock();
        try {
            if(list.isEmpty()) {
                serverList = null;
                return;
            }

            int length = 0;
            for(ServerInfo info : list) {
                for(int i = 0; i < info.weight; i++) {
                    if(info.weight <= 0) {
                        info.weight = 1;
                    }
                    length += info.weight;
                }
            }

            int index = 0;
            serverList = new ServerInfo[length];
            for(ServerInfo info : list) {
                for(int i = 0; i < info.weight; i++) {
                    serverList[index++] = new ServerInfo(info.version, info.ip, info.port, info.weight);
                }
            }
        } finally {
            writeLock.unlock();
        }

    }

    public ServerInfo select() {
        readLock.lock();
        try {
            if(serverList == null || serverList.length == 0) {
                return null;
            }

            int length = serverList.length;
            if(length == 1) {
                return serverList[0];
            } else {
                return serverList[new Random().nextInt(length)];
            }
        } finally {
            readLock.unlock();
        }
    }
}
