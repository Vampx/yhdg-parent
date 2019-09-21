package cn.com.yusong.yhdg.webserver.biz.client;

import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_12;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_22;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BizWEB_22 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg_WEB_22 msg = (Msg_WEB_22) message;
        RespBody<Msg_WEB_22> respBody = ClientBizUtils.respVehicleBodyPool.get(msg.vinNo);
        if(respBody != null) {
            respBody.setBody(msg);
        }
    }
}
