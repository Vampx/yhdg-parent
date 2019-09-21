package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000008;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000008;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000008;
import cn.com.yusong.yhdg.frontserver.biz.server.AbstractBiz;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 终端有新命令
 */
@Component
public class Biz931000008 extends AbstractBiz {

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000008 request = (Msg931000008) message;
        Msg932000008 response = new Msg932000008();
        response.setSerial(request.getSerial());

        Msg911000008 msg = new Msg911000008();
        msg.setSerial((int) sequence.incrementAndGet());

        config.sessionManager.writeAndFlush(request.terminalId, msg);

        writeAndFlush(context, response);
    }

}
