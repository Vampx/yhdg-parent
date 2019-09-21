package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000006;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 心跳
 */
@Component
public class Biz221000006 extends AbstractBiz implements HighPriorityBiz {

    static final Logger log = LogManager.getLogger(Biz221000006.class);

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg221000006 request = (Msg221000006) message;
        if(request.clientType == Msg221000006.CLIENT_TYPE_STATIC_SERVER) {
            if(log.isDebugEnabled()) {
                log.debug("static-server连接成功");
            }

            if(sessionManager.getStaticServerClient() == null || sessionManager.getStaticServerClient() != context) {
                sessionManager.setStaticServerClient(context);
            }
        }
    }
}
