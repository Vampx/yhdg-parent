package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000017;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000017;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.entity.StrategyXml;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalStrategyService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询策略内容
 */
@Component
public class Biz921000017 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000017.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalStrategyService terminalStrategyService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000017 request = (Msg921000017)message;
        Msg922000017 response = new Msg922000017();
        response.setSerial(request.getSerial());

        TerminalSession session = getSession(attributes);
        if(session == null) {
            response.rtnCode = RespCode.CODE_7.getValue();
            response.rtnMsg = RespCode.CODE_7.getName();
            writeAndFlush(context, response);
            return;
        }
        log.debug("terminal ID {} ", session.id);

        Terminal terminal = terminalService.find(session.id);
        if(terminal == null) {
            response.rtnCode = RespCode.CODE_3.getValue();
            response.rtnMsg = RespCode.CODE_3.getName();
            writeAndFlush(context, response);
            return;
        }

        if(terminal.getStrategyId() != null) {
            StrategyXml strategyXml = terminalStrategyService.findStrategyXml(terminal.getStrategyId());
            response.uid = strategyXml.uid;
            response.strategy = strategyXml.xml;
        }

        writeAndFlush(context, response);
    }
}
