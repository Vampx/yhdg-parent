package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询参数通知
 */
@Component
public class BizT14 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T14 msg = (Msg_T14) message;

        Resp resp = respPool.get(msg.vinNo);
        if(resp != null) {
            respPool.remove(msg.vinNo);
            Msg_WEB_21 request = (Msg_WEB_21) resp.msg;
            Msg_WEB_22 response = new Msg_WEB_22();
            response.time = request.time;
            response.vinNo = request.vinNo;
            response.returnValue = msg.returnValue;
            writeAndFlush(resp.context, response);
        } else {
            log.warn("serial {} resp not exists", msg.vinNo);
        }
    }
}
