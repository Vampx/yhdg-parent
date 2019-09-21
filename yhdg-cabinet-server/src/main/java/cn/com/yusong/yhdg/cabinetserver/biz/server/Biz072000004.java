package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg07.Msg072000002;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg072000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000005;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000005;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开关门查询通知
 */
@Component
public class Biz072000004 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg072000004 msg = (Msg072000004) message;

        Resp resp = respPool.get(msg.getSerial());
        if(resp != null) {
            respPool.remove(msg.getSerial());
            Msg221000005 request = (Msg221000005) resp.msg;
            Msg222000005 response = new Msg222000005();
            response.setSerial(request.getSerial());
            response.rtnCode = msg.rtnCode;

            writeAndFlush(resp.context, response);
        } else {
            log.warn("serial {} resp not exists", msg.getSerial());
        }
    }
}
