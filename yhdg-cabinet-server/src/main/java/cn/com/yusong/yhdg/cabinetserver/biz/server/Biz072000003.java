package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg07.Msg072000001;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg072000003;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000004;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开门通知
 */
@Component
public class Biz072000003 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg072000003 msg = (Msg072000003) message;

        Resp resp = respPool.get(msg.getSerial());
        if(resp != null) {
            respPool.remove(msg.getSerial());
            Msg221000004 request = (Msg221000004) resp.msg;
            Msg222000004 response = new Msg222000004();
            response.setSerial(request.getSerial());
            response.rtnCode = msg.rtnCode;
            response.boxStatus = msg.boxStatus;
            writeAndFlush(resp.context, response);
        } else {
            log.warn("serial {} resp not exists", msg.getSerial());
        }
    }
}
