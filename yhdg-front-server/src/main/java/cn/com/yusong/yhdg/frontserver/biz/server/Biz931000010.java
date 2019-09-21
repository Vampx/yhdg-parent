package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg91.Msg911000010;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg931000010;
import cn.com.yusong.yhdg.common.protocol.msg93.Msg932000010;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalUploadLogService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 柜子配置更新
 */
@Component
public class Biz931000010 extends AbstractBiz {

    @Autowired
    TerminalUploadLogService terminalUploadLogService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg931000010 request = (Msg931000010) message;
        Msg932000010 response = new Msg932000010();
        response.setSerial(request.getSerial());

        Msg911000010 msg = new Msg911000010();
        msg.setSerial((int) sequence.incrementAndGet());
        msg.address = request.address;
        msg.tel = request.tel;
        config.sessionManager.writeAndFlush(request.terminalId, msg);

        writeAndFlush(context, response);
    }
}
