package cn.com.yusong.yhdg.frontserver.biz.server;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 终端有新命令
 */
@Component
public class Biz912000008 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        //do nothing
    }
}