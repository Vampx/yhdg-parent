package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.*;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 修改参数通知
 */
@Component
public class BizT2 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T2 msg = (Msg_T2) message;

        Resp resp = respPool.get(msg.vinNo);
        if(resp != null) {
            respPool.remove(msg.vinNo);
            Msg_WEB_31 request = (Msg_WEB_31) resp.msg;
            Msg_WEB_32 response = new Msg_WEB_32();
            response.time = request.time;
            response.vinNo = request.vinNo;
            response.returnValue = msg.returnValue;
            response.code = msg.code;
            writeAndFlush(resp.context, response);
        } else {
            log.warn("serial {} resp not exists", msg.vinNo);
        }
    }
}
