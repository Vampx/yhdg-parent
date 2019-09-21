package cn.com.yusong.yhdg.frontserver.biz.server;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.common.protocol.msg92.Interface921000027;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg921000027;
import cn.com.yusong.yhdg.common.protocol.msg92.Msg922000027;
import cn.com.yusong.yhdg.frontserver.comm.session.TerminalSession;
import cn.com.yusong.yhdg.frontserver.constant.RespCode;
import cn.com.yusong.yhdg.frontserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalScreenSnapshotService;
import cn.com.yusong.yhdg.frontserver.service.yms.TerminalService;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 上传截屏
 */
@Component
public class Biz921000027 extends AbstractBiz {
    static Logger log = LoggerFactory.getLogger(Biz921000027.class);

    @Autowired
    TerminalService terminalService;
    @Autowired
    TerminalScreenSnapshotService terminalScreenSnapshotService;
    @Autowired
    CabinetService cabinetService;

    public final static String PATH = "/static/download/";

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg921000027 request = (Msg921000027) message;
        Msg922000027 response = new Msg922000027();
        response.setSerial(request.getSerial());
        if(log.isDebugEnabled()) {
            log.debug("上传截屏:{}", request.fileList);
        }

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

        Cabinet cabinet = cabinetService.findTerminal(session.id);
        for (Msg921000027.File e : request.fileList) {
            String time = StringUtils.substringBetween(e.path, "[", "]");
            TerminalScreenSnapshot snapshot = new TerminalScreenSnapshot();
            snapshot.setSnapshotPath(PATH + e.path);
            if (cabinet != null) {
                snapshot.setAgentId(cabinet.getAgentId());
            }

            snapshot.setCreateTime(new Date());
            snapshot.setTerminalId(terminal.getId());
            snapshot.setSnapTime(new Date(Long.parseLong(time)));
            terminalScreenSnapshotService.insert(snapshot);
        }

        response.rtnCode = RespCode.CODE_0.getValue();
        writeAndFlush(context, response);
        return;
    }

}
