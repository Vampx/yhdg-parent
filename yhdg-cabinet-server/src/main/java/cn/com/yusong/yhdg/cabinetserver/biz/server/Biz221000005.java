package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg071000002;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg221000005;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000005;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 开关门查询通知 中转包
 */
@Component
public class Biz221000005 extends AbstractBiz implements HighPriorityBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg221000005 request = (Msg221000005) message;
        Msg222000005 response = new Msg222000005();
        response.setSerial(request.getSerial());

        CabinetSession session = sessionManager.getCabinetSession(request.cabinetId);
        if (session != null) {
            Msg071000002 msg = new Msg071000002();
            msg.setSerial(request.getSerial());

            respPool.put(msg.getSerial(), new Resp(context, attributes, request));
            session.writeAndFlush(msg);

        } else {
            response.rtnCode = TsRespCode.CODE_3.getValue();
            context.writeAndFlush(response);
        }
    }
}
