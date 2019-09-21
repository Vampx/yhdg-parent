package cn.com.yusong.yhdg.serviceserver.biz.server;

import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.protocol.msg21.Msg211000001;
import cn.com.yusong.yhdg.common.protocol.msg21.Msg212000001;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.service.basic.MobileMessageService;
import cn.com.yusong.yhdg.serviceserver.tool.sms.Param;
import cn.com.yusong.yhdg.serviceserver.tool.sms.Result;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 发送短信
 */
@Component
public class Biz211000001 extends AbstractBiz {

    Logger log = LogManager.getLogger(Biz211000001.class);

    @Autowired
    AppConfig config;
    @Autowired
    MobileMessageService mobileMessageService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg211000001 request = (Msg211000001) message;
        Msg212000001 response = new Msg212000001();
        response.setSerial(request.getSerial());

        Param param = new Param();
        param.partnerId = request.partnerId;
        param.mobile = request.mobile;
        param.content = request.content;
        param.vairable = request.variable;
        param.templateCode = request.templateCode;

        Result result = config.sms.send(param);
        if(result == null) {
            response.rtnCode = (short) 1;
            response.rtnMsg = "没有可用的短信接口";
        } else {
            response.rtnCode = result.success ? (short) 0 : (short) 1;
            //TODO 这里如果用 维纳多 是收不到消息回执的
            insertMobileMessage(request.partnerId, request.moduleId, request.sourceType, request.sourceId, param.mobile, param.content, result.success, result.id, result.msgId, request.type, MobileMessage.DELAY_TIME_ZERO, request.variable, request.templateCode);
        }

        writeAndFlush(context, response);
    }

    private void insertMobileMessage(int partnerId, int moduleId, int sourceType, String sourceId, String mobile, String content, boolean success, int senderId, String msgId, int type, int delay, String variable, String templateCode) {
        Date now = new Date();
        MobileMessage mobileMessage = new MobileMessage();
        mobileMessage.setPartnerId(partnerId);
        mobileMessage.setCreateTime(now);
        mobileMessage.setHandleTime(now);
        mobileMessage.setStatus(success ? MobileMessage.MessageStatus.OK.getValue() : MobileMessage.MessageStatus.FAIL.getValue());
        if(success) {
            mobileMessage.setMsgId(msgId);
        }
        mobileMessage.setSourceId(sourceId);
        mobileMessage.setSourceType(sourceType);
        mobileMessage.setMobile(mobile);
        mobileMessage.setContent(content);
        mobileMessage.setSenderId(senderId);
        mobileMessage.setType(type);
        mobileMessage.setDelay(delay);
        mobileMessage.setVariable(variable);
        mobileMessage.setTemplateCode(templateCode);

        if(content.indexOf("${") >= 0) {
            log.error("mobile message error: {}", content);
        }

        mobileMessageService.insert(mobileMessage);
    }
}
