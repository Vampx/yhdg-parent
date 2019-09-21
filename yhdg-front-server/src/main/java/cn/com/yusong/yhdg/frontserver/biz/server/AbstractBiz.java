package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.frontserver.comm.session.Session;
import cn.com.yusong.yhdg.frontserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractBiz extends Biz {

    static final Logger log = LogManager.getLogger(AbstractBiz.class);

    @Autowired
    SessionManager sessionManager;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    public AppConfig config;

    public TerminalSession getSession(Map<String, Object> attributes) {
        return (TerminalSession) attributes.get(Session.SESSION_KEY);
    }
    public static AtomicLong sequence = new AtomicLong();
    public static Map<Integer, Resp> respPool = new ConcurrentHashMap<Integer, Resp>();
    public static final Runnable SCAN_TASK = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            List<Integer> expireList = new LinkedList<Integer>();
            for(Map.Entry<Integer, Resp> kv : respPool.entrySet()) {
                if(now - kv.getValue().timestamp >= 1000 * 15) {
                    expireList.add(kv.getKey());
                }
            }
            for(Integer k : expireList) {
                respPool.remove(k);
            }

            if(log.isDebugEnabled()) {
                log.debug("respPool.size = {}", respPool.size());
            }


            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e) {
            }
        }
    };

    static {
        Thread thread = new Thread(SCAN_TASK);
        thread.start();
    }

    public static class Resp {
        public ChannelHandlerContext context;
        public Map<String, Object> attributes;
        public Message msg;
        public long timestamp = System.currentTimeMillis();

        public Resp(ChannelHandlerContext context, Map<String, Object> attributes, Message msg) {
            this.context = context;
            this.attributes = attributes;
            this.msg = msg;
        }
    }

    public String getTerminalId(Map<String, Object> attributes) {
        Session session = (Session) attributes.get(Session.SESSION_KEY);
        if(session != null) {
            return session.id;
        } else {
            return null;
        }
    }

}
