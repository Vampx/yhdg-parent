package cn.com.yusong.yhdg.agentserver.comm.server;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FrontServerSession extends Session {

    static final Logger log = LogManager.getLogger(FrontServerSession.class);

    public int frontServerId; //front server id

    public Set<TerminalSession> terminalSessionList = new CopyOnWriteArraySet<TerminalSession>();

    public FrontServerSession(SessionManager sessionManager, ChannelHandlerContext handlerContext, Map<String, Object> attributes, SessionId id, int frontServerId) {
        super(sessionManager, handlerContext, attributes, id);

        this.frontServerId = frontServerId;
        this.handlerContext = handlerContext;
        this.attributes = attributes;
        attributes.put(SESSION_KEY, this);
    }

    @Override
    public void close() {
        sessionManager.removeFromMap(id);

        attributes.remove(SESSION_KEY);
        if(log.isDebugEnabled()) {
            log.debug("FrontServerSession invoke close()");
        }

        handlerContext.close();

        for(TerminalSession session : terminalSessionList) {
            session.close();
        }
        terminalSessionList.clear();


    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
