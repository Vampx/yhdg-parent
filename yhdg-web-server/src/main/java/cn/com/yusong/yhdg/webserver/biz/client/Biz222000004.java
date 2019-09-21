package cn.com.yusong.yhdg.webserver.biz.client;

import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Biz222000004 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg222000004 msg = (Msg222000004) message;
        RespBody<Msg222000004> respBody = ClientBizUtils.respBodyPool.get(msg.getSerial());
        if(respBody != null) {
            respBody.setBody(msg);
        }
    }
}
