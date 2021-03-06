package cn.com.yusong.yhdg.vehicleserver.comm.session;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.vehicleserver.service.zc.VehicleOnlineStatsService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SessionManager {


    static Logger log = LoggerFactory.getLogger(SessionManager.class);

    public static long TIMEOUT = 60L * 5 * 1000;

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    VehicleOnlineStatsService vehicleOnlineStatsService;

    private Map<String, Session> sessionMap;
    private ScheduledExecutorService scheduledExecutorService;

    public SessionManager() {
        super();
        sessionMap = new HashMap<String, Session>(100);
       // scheduledExecutorService = Executors.newScheduledThreadPool(2);
        //心跳超时暂不判断
        //scheduledExecutorService.scheduleAtFixedRate(new RefreshThread(), 0, TIMEOUT, TimeUnit.MILLISECONDS);


    }

    public synchronized VehicleSession getVehicleSession(String id) {
        return (VehicleSession) sessionMap.get(id);
    }

    public synchronized VehicleSession addVehicleSession(ChannelHandlerContext context, Map<String, Object> attributes, String id) {

        VehicleSession session = (VehicleSession) sessionMap.get(id);
        if (session == null) {
            session = new VehicleSession(context, attributes, id);
            sessionMap.put(session.id, session);
        } else {
            if (session.handlerContext != context) {
                session.handlerContext = context;
            }
            if (session.attributes != attributes) {
                attributes.putAll(session.attributes);
                session.attributes = attributes;
            }
            session.refreshHeartTime();
        }

        return session;
    }

    public synchronized void writeAndFlush(Message message) {
        for (Session session : sessionMap.values()) {
            session.writeAndFlush(message);
        }
    }

    public synchronized void remove(String id) {
        final Session session = sessionMap.get(id);
        if (session != null) {
            sessionMap.remove(id);
            vehicleOnlineStatsService.end(id);
            memCachedClient.delete(CacheKey.key(CacheKey.K_VEHICLE_ID_V_LOGIN_SERVER, id));
            session.close();
        }
    }

    public synchronized Map<String, Session> getSessionMap() {
        Map<String, Session> map = new HashMap<String, Session>();
        for (Session session : sessionMap.values()) {
            map.put(session.id, session);
        }
        return map;
    }


    public synchronized void clear() {
        Map<String, Session> map = getSessionMap();
        for (Map.Entry<String, Session> entry : map.entrySet()) {
            remove(entry.getKey());
        }
    }

    public synchronized void close() {
        scheduledExecutorService.shutdown();
    }

    private class RefreshThread implements Runnable {

        public void run() {
            refresh();
        }

        void refresh() {
            List<Session> timeoutList = new ArrayList<Session>();
            for (Session session : sessionMap.values()) {
                if (System.currentTimeMillis() - session.heartTime > TIMEOUT) {
                    if (log.isDebugEnabled()) {
                        log.debug("session 超时, 心跳时间: {}", DateFormatUtils.format(session.heartTime, "yyyy-MM-dd HH:mm:ss"));
                    }
                    timeoutList.add(session);
                }
            }

            if (!timeoutList.isEmpty()) {
                for (Session s : timeoutList) {
                    SessionManager.this.remove(s.id);
                }
            }
        }
    }
}
