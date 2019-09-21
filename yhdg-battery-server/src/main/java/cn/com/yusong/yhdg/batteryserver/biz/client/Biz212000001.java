package cn.com.yusong.yhdg.batteryserver.biz.client;

import cn.com.yusong.yhdg.common.entity.RespBody;
import cn.com.yusong.yhdg.common.protocol.msg21.Msg212000001;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Biz212000001 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg212000001 msg = (Msg212000001) message;
        RespBody<Msg212000001> respBody = ClientBizUtils.respBodyPool.get(msg.getSerial());
        if(respBody != null) {
            respBody.setBody(msg);
        }
    }
}