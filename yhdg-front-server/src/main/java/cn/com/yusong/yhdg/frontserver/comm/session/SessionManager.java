package cn.com.yusong.yhdg.frontserver.comm.session;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000002;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalOnlineService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    static Logger log = LoggerFactory.getLogger(SessionManager.class);

    public static long TIMEOUT = 60L * 5 * 1000;

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
    @Autowired
    TerminalService terminalService;

    private Map<String, TerminalSession> sessionMap;
    private ScheduledExecutorService scheduledExecutorService;
    private Set<String> downloadQueue;
    private Queue<DownloadSession> waitDownloadQueue;

    public SessionManager() {
        super();
        sessionMap = new HashMap<String, TerminalSession>(1024);
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new RefreshThread(), 0, 1, TimeUnit.MINUTES);

        downloadQueue = new HashSet<String>(200);

        waitDownloadQueue = new PriorityQueue<DownloadSession>(500);
    }

    public synchronized Session getSession(String id) {
        return sessionMap.get(id);
    }

    public synchronized TerminalSession getTerminalSession(String id) {
        return (TerminalSession) sessionMap.get(id);
    }

    public synchronized TerminalSession addTerminalSession(ChannelHandlerContext context, Map<String, Object> attributes, String id) {

        TerminalSession session = sessionMap.get(id);
        if (session == null) {
            session = new TerminalSession(context, attributes, id);
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

    public synchronized void writeAndFlush(String id, Message message) {
        Session session = sessionMap.get(id);
        if(session != null) {
            session.writeAndFlush(message);
            if(log.isDebugEnabled()) {
                log.debug("sent {} msg: {}", id, message);
            }
        }
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
            terminalService.offline(id);
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_TERMINAL_FRONT_LOGIN_SERVER, id));
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

    public synchronized boolean isPermitDownload(String id, int playlistId) {
        DownloadSession session = new DownloadSession(id, playlistId);
        Set<String> total = new HashSet<String>();

        log.info("SessionManager终端ID ：{}！", session.id);
        log.info("downloadQueue ：{}！", downloadQueue);
        if(downloadQueue.contains(session.id)) {
            return true;
        } else {
            if(!waitDownloadQueue.contains(session)) {
                waitDownloadQueue.add(session);
            }

            while (downloadQueue.size() < config.downloadCount) {
                DownloadSession element = waitDownloadQueue.poll();
                if(element == null) {
                    break;
                }
                total.add(element.id);
                downloadQueue.add(element.id);
            }
        }

        for(String e : total) {
            noticePermitDownload(e);
        }

        return total.contains(id);
    }

    private void noticePermitDownload(String id) {
        Msg911000002 msg = new Msg911000002();
        TerminalSession s = sessionMap.get(id);
        if(s != null) {
            msg.playlistId = s.playlistId;
            s.writeAndFlush(msg);
        }
    }

    public synchronized void finishedDownload(String id) {
        downloadQueue.remove(id);
        Iterator<DownloadSession> iterator = waitDownloadQueue.iterator();
        while (iterator.hasNext()) {
            DownloadSession e = iterator.next();
            if(e.id.equals(id)) {
                iterator.remove();
            }
        }
        tryJoinDownload();
    }

    public synchronized Set<String> tryJoinDownload() {
        Set<String> total = new HashSet<String>();

        while (downloadQueue.size() < config.downloadCount) {
            DownloadSession element = waitDownloadQueue.poll();
            if(element == null) {
                break;
            }
            total.add(element.id);
            downloadQueue.add(element.id);
        }

        for(String e : total) {
            noticePermitDownload(e);
        }

        return total;
    }
}
