package cn.com.yusong.yhdg.webserver.comm.server;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalOnlineService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class TerminalSession extends Session {

    static final Logger log = LogManager.getLogger(TerminalSession.class);

    public String terminalId; //终端id

    public FrontServerSession frontServerSession;

    public TerminalSession(SessionManager sessionManager, FrontServerSession frontServerSession, Map<String, Object> attributes, SessionId id, String terminalId) {
        super(sessionManager, frontServerSession.handlerContext, attributes, id);

        this.frontServerSession = frontServerSession;
        this.terminalId = terminalId;
        this.handlerContext = frontServerSession.handlerContext;
        this.attributes = attributes;

        frontServerSession.terminalSessionList.add(this);
    }

    @Override
    public void close() {
        if(log.isDebugEnabled()) {
            log.debug("TerminalSession invoke close()");
        }
        TerminalOnlineService terminalOnlineService = SpringContextHolder.getBean(TerminalOnlineService.class);
        terminalOnlineService.offline(terminalId);
        sessionManager.removeFromMap(id);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
