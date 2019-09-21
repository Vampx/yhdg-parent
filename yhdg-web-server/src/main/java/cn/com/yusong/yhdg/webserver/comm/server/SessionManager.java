package cn.com.yusong.yhdg.webserver.comm.server;

import cn.com.yusong.yhdg.common.tool.netty.Message;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    static Logger log = LoggerFactory.getLogger(SessionManager.class);

    public static long TIMEOUT = 60L * 5 * 1000;

    private Map<SessionId, Session> sessionMap;
    private ScheduledExecutorService scheduledExecutorService;

    public SessionManager() {
        super();
        sessionMap = new ConcurrentHashMap<SessionId, Session>(100);
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(new RefreshThread(), 0, TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public Session getSession(SessionId id) {
        return sessionMap.get(id);
    }

    public TerminalSession getTerminalSession(String terminalId) {
        SessionId id = new SessionId(SessionId.TERMINAL_SESSION, terminalId);
        return (TerminalSession) sessionMap.get(id);
    }

    public void remove(byte type, Object id) {
        SessionId sessionId = new SessionId(type, id);
        final Session session = sessionMap.get(sessionId);
        if(session != null) {
            sessionMap.remove(sessionId);
            session.close();
        }
    }

    public void remove(SessionId sessionId) {
        Session session = removeFromMap(sessionId);
        if(session != null) {
            session.close();
        }
    }

    public Session removeFromMap(SessionId sessionId) {
        if(log.isDebugEnabled()) {
            log.debug("removeFromMap, sessionId:{}", sessionId.id);
        }
        return sessionMap.remove(sessionId);
    }

    public void writeByAgent(byte sessionType, Message message) {
        for (Session session : sessionMap.values()) {
            if (session.id.type == sessionType) {
                session.writeAndFlush(message);
            }
        }
    }

    public void writeByTerminal(String terminalId, Message message) {
        TerminalSession session = getTerminalSession(terminalId);
        if(session != null) {
            session.writeAndFlush(message);
        }
    }

    public Map<SessionId, Session> getSessionMap() {
        Map<SessionId, Session> map = new HashMap<SessionId, Session>();
        for(Session session : sessionMap.values()) {
            map.put(session.id, session);
        }
        return map;
    }


    public void clear() {
        Map<SessionId, Session> map = getSessionMap();
        for(Map.Entry<SessionId, Session> entry : map.entrySet()) {
            remove(entry.getKey());
        }
    }

    public void close() {
        scheduledExecutorService.shutdown();
    }

    private class RefreshThread implements Runnable {

        public void run() {
            refresh();
        }

        void refresh() {
            List<Session> timeoutList = new ArrayList<Session>();
            for(Session session : sessionMap.values()) {
                if(System.currentTimeMillis() - session.heartTime > TIMEOUT) {
                    if(log.isDebugEnabled()) {
                        log.debug("session 超时, 心跳时间: {}", DateFormatUtils.format(session.heartTime, "yyyy-MM-dd HH:mm:ss"));
                    }
                    timeoutList.add(session);
                }
            }

            if(!timeoutList.isEmpty()) {
                for(Session s : timeoutList) {
                    SessionManager.this.remove(s.id);
                }
            }
        }
    }
}
