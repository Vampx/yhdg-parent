package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.*;
import cn.com.yusong.yhdg.vehicleserver.comm.session.VehicleSession;
import cn.com.yusong.yhdg.vehicleserver.service.zc.VehicleService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
* 查询参数 中转包
*
 */
@Log4j2
@Component
public class BizWEB_21 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_21 request = (Msg_WEB_21) message;
        Msg_WEB_22 response = new Msg_WEB_22();
        response.time = request.time;
        response.vinNo = request.vinNo;

        VehicleSession session = sessionManager.getVehicleSession(request.vinNo);
        if (session != null) {
            Msg_S14 msg = new Msg_S14();
            msg.value = request.value;
            respPool.put(request.vinNo, new Resp(context, attributes, request));
            session.writeAndFlush(msg);
        } else {
            response.returnValue = "终端响应超时";
            writeAndFlush(context, response);
        }
    }
}
