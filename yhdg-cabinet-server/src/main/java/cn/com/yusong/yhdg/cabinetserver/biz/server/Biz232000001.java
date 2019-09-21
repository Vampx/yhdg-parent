package cn.com.yusong.yhdg.cabinetserver.biz.server;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Biz232000001 extends AbstractBiz implements HighPriorityBiz {
    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        //do nothing
    }
}
