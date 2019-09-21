package cn.com.yusong.yhdg.serviceserver.biz.server;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 心跳
 */
@Component
public class Biz211000002 extends AbstractBiz {
    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        //do nothing
    }
}
