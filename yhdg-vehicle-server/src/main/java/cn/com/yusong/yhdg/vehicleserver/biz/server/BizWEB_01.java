package cn.com.yusong.yhdg.vehicleserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_02;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_01;
import cn.com.yusong.yhdg.vehicleserver.service.zc.VehicleService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
* web心跳
*
 */
@Log4j2
@Component
public class BizWEB_01 extends AbstractBiz {

    @Autowired
    VehicleService vehicleService;



    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_01 request = (Msg_WEB_01) message;
        Msg_WEB_02 response = new Msg_WEB_02();
        response.time = request.time;

        log.debug("web-server连接成功");
    }
}
