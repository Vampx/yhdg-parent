package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.TextMessage;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.vehicleserver.comm.session.Session;
import cn.com.yusong.yhdg.vehicleserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.vehicleserver.config.AppConfig;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBiz extends Biz {

    static final Logger log = LogManager.getLogger(AbstractBiz.class);

    @Autowired
    SessionManager sessionManager;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;

    public void updateLoginServer(Session session) {
        String serverAddress = config.getServerIp() + ":" + config.getServerPort();
        memCachedClient.set(CacheKey.key(CacheKey.K_VEHICLE_ID_V_LOGIN_SERVER, session.id), serverAddress, MemCachedConfig.CACHE_FIVE_MINUTE * 2);
    }

    protected void writeAndFlush(final ChannelHandlerContext context, final TextMessage message) {
        log.debug("begin sent to{}, {}", context.channel().remoteAddress(), message.encode());

        ChannelFuture future = context.writeAndFlush(message);
        if(log.isDebugEnabled()) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.debug("complete sent to {}", context.channel().remoteAddress());
                }
            });
        }
    }

    protected void writeAndClose(final ChannelHandlerContext context, final TextMessage message) {
        ChannelFuture future = context.writeAndFlush(message);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(log.isDebugEnabled()) {
                    log.debug("sent to {}, {}", context.channel().remoteAddress(),  message.encode());
                }
                context.close();
                if(log.isDebugEnabled()) {
                    log.debug("invoke close()");
                }
            }
        });
    }

    public static Map<String, Resp> respPool = new ConcurrentHashMap<String, Resp>();
    public static final Runnable SCAN_TASK = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            List<String> expireList = new LinkedList<String>();
            for(Map.Entry<String, Resp> kv : respPool.entrySet()) {
                if(now - kv.getValue().timestamp >= 1000 * 15) {
                    expireList.add(kv.getKey());
                }
            }
            for(String k : expireList) {
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
        public TextMessage msg;
        public long timestamp = System.currentTimeMillis();

        public Resp(ChannelHandlerContext context, Map<String, Object> attributes, TextMessage msg) {
            this.context = context;
            this.attributes = attributes;
            this.msg = msg;
        }
    }

}
