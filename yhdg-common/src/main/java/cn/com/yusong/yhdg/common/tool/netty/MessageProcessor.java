package cn.com.yusong.yhdg.common.tool.netty;

import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class MessageProcessor implements Runnable {

    private final Logger log = LogManager.getLogger(MessageProcessor.class);

    private long submitTime = 0;
    private long beginTime = 0;
    private long endTime = 0;
    private boolean executeSuccess = true;

    public ChannelHandlerContext channelHandlerContext;
    public Object message;
    public Biz biz;
    public Map<String, Object> attributes;

    public MessageProcessor() {
        submitTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        before();
        try {
            biz.doBiz(channelHandlerContext, attributes, message);
        } catch (Throwable e) {
            executeSuccess = false;
            log.error("MessageProcessor execute error", e);
            e.printStackTrace();
        } finally {
            after();
        }
    }

    protected void before() {
        beginTime = System.currentTimeMillis();
        if(log.isInfoEnabled()) {
            log.info("biz {} begin, submit: {}ms", biz, beginTime - submitTime);
        }
    }

    protected void after() {
        endTime = System.currentTimeMillis();
        if(log.isInfoEnabled()) {
            log.info("biz {} {} end", biz, executeSuccess ? "success" : "fail");
            log.info("biz {} submit: {}ms, run:{}ms", biz, beginTime - submitTime, endTime - beginTime);
        }
    }
}