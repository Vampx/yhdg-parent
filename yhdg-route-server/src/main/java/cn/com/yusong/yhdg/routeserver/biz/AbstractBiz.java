package cn.com.yusong.yhdg.routeserver.biz;

import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBiz extends Biz {

    static final Logger log = LogManager.getLogger(cn.com.yusong.yhdg.routeserver.biz.AbstractBiz.class);
    @Autowired
    AppConfig config;

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

}
