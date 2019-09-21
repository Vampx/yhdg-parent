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
* 锁车通知 中转包
*
 */
@Log4j2
@Component
public class BizWEB_11 extends AbstractBiz {

    @Autowired
    VehicleService vehicleService;



    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_11 request = (Msg_WEB_11) message;
        Msg_WEB_12 response = new Msg_WEB_12();
        response.time = request.time;
        response.vinNo = request.vinNo;

        VehicleSession session = sessionManager.getVehicleSession(request.vinNo);
        if (session != null) {
            Msg_S15 msg = new Msg_S15();
            if(request.status == Vehicle.LockSwitch.NO.getValue()
                    || request.status == Vehicle.LockSwitch.DISCHG_OPEN.getValue() ){
                msg.lock = 0;
            }else{
                msg.lock = 1;
            }
            respPool.put(request.vinNo, new Resp(context, attributes, request));
            session.writeAndFlush(msg);
        } else {
            response.code =4;
            writeAndFlush(context, response);
        }
    }
}
