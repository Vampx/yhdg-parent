package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_T15;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_12;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_11;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 锁车通知
 */
@Component
public class BizT15 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_T15 msg = (Msg_T15) message;

        Resp resp = respPool.get(msg.vinNo);
        if(resp != null) {
            respPool.remove(msg.vinNo);
            Msg_WEB_11 request = (Msg_WEB_11) resp.msg;
            Msg_WEB_12 response = new Msg_WEB_12();
            response.time = request.time;
            response.vinNo = request.vinNo;

            if(msg.code == 1){
                response.code = 0;
            }else{
                response.code = 8;
            }
            writeAndFlush(resp.context, response);
        } else {
            log.warn("serial {} resp not exists", msg.vinNo);
        }
    }
}
