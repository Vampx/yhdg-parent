package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.*;
import cn.com.yusong.yhdg.vehicleserver.comm.session.VehicleSession;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
* 设置参数 中转包
*
 */
@Log4j2
@Component
public class BizWEB_31 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_31 request = (Msg_WEB_31) message;
        Msg_WEB_32 response = new Msg_WEB_32();
        response.time = request.time;
        response.vinNo = request.vinNo;

        VehicleSession session = sessionManager.getVehicleSession(request.vinNo);
        if (session != null) {
            Msg_S2 msg = new Msg_S2();
            msg.value = request.value;
            respPool.put(request.vinNo, new Resp(context, attributes, request));
            session.writeAndFlush(msg);
        } else {
            response.returnValue = "终端响应超时";
            writeAndFlush(context, response);
        }
    }
}
