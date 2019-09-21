package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.protocol.msg92.Interface922000025;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000025;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000025;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalCommandService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询未执行命令
 */
@Component
public class Biz921000025 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000025.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalCommandService terminalCommandService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000025 request = (Msg921000025)message;
        Msg922000025 response = new Msg922000025();
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

        List<TerminalCommand> terminalCommandList = terminalCommandService.findWaitExec(terminal.getId(), TerminalCommand.Status.WAIT.getValue());
        for(TerminalCommand command : terminalCommandList) {
            Interface922000025.Command cmd = new Interface922000025.Command();
            cmd.id = command.getId().intValue();
            cmd.type = command.getType().byteValue();
            cmd.content = command.getContent();
            response.commandList.add(cmd);

            terminalCommandService.dispatch(command.getId(), TerminalCommand.Status.DISPATCH.getValue(), new Date());
        }

        writeAndFlush(context, response);
    }
}
