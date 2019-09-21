package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000022;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000022;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 完成下载
 */
@Component
public class Biz921000022 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000022.class);

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000022 request = (Msg921000022) message;
        Msg922000022 response = new Msg922000022();
        response.setSerial(request.getSerial());

        String terminalId = getTerminalId(attributes);
        log.debug("terminal ID {} ", terminalId);

        if(log.isDebugEnabled()) {
            log.debug(" 终端：{} download playlist end!", terminalId);
        }
        if (StringUtils.isEmpty(terminalId)) {
            response.rtnCode = RespCode.CODE_3.getValue();
            writeAndFlush(context, response);
            return;
        }
        config.sessionManager.finishedDownload(terminalId);
        writeAndFlush(context, response);
    }
}
