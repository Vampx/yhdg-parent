package cn.com.yusong.yhdg.webserver.biz.client;

import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_12;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BizWEB_12 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_12 msg = (Msg_WEB_12) message;
        RespBody<Msg_WEB_12> respBody = ClientBizUtils.respVehicleBodyPool.get(msg.vinNo);
        if(respBody != null) {
            respBody.setBody(msg);
        }
    }
}
