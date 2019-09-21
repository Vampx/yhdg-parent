package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000001;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000001;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000001;
import cn.com.yusong.yhdg.frontserver.biz.server.AbstractBiz;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 策略内容更新
 */
@Component
public class Biz931000001 extends AbstractBiz {
    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000001 request = (Msg931000001) message;
        Msg932000001 response = new Msg932000001();
        response.setSerial(request.getSerial());

        Msg911000001 msg = new Msg911000001();
        msg.setSerial((int) sequence.incrementAndGet());
        msg.uid = request.uid;
        config.sessionManager.writeAndFlush(request.terminalId, msg);

        writeAndFlush(context, response);
    }
}
