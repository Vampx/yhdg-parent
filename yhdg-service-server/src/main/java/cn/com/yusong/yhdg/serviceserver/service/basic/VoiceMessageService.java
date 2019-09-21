package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.domain.basic.VoiceMessage;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.VoiceMessageMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import cn.com.yusong.yhdg.serviceserver.tool.voice.Param;
import cn.com.yusong.yhdg.serviceserver.tool.voice.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VoiceMessageService extends AbstractService {
    static Logger log = LogManager.getLogger(VoiceMessageService.class);

    @Autowired
    private VoiceMessageMapper voiceMessageMapper;
    @Autowired
    private AppConfig config;

    @Transactional(rollbackFor = Throwable.class)
    public void scanMessage() {
        int offset = 0, limit = 100;

        while (true) {
            List<VoiceMessage> messageList = voiceMessageMapper.findList(VoiceMessage.MessageStatus.NOT.getValue(), offset, limit);
            if(messageList.isEmpty()) {
                break;
            }

            for(VoiceMessage message : messageList) {
                Param param = new Param();
                param.agentId = message.getAgentId();
                param.id = message.getId();
                param.templateCode = message.getTemplateCode();
                param.calledShowNumber = message.getCalledShowNumber();
                param.calledNumber = message.getCalledNumber();
                param.variable = message.getVariable();
                param.volume = message.getVolume();
                param.playTimes = message.getPlayTimes();

                VoiceConfig account = getVoiceConfig(param.agentId);
                if (account == null) {
                    voiceMessageMapper.complete(message.getId(), VoiceMessage.MessageStatus.FAIL.getValue(), null, message.getId().intValue());
                    log.error("Not VoiceConfig, agentId = {}", param.agentId);
                    continue;
                }

                Result result = config.aliyunVoiceClient.send(account, param);
                if(result == null) {
                    voiceMessageMapper.complete(message.getId(), VoiceMessage.MessageStatus.FAIL.getValue(), null, message.getId().intValue());
                    log.error("Not available VoiceConfig, agentId = {}", param.agentId);
                    continue;
                }

                if(result.success) {
                    voiceMessageMapper.complete(message.getId(), VoiceMessage.MessageStatus.OK.getValue(), new Date(), result.id);
                    voiceMessageMapper.updateMsgId(message.getId(), result.msgId);
                } else {
                    voiceMessageMapper.complete(message.getId(), VoiceMessage.MessageStatus.FAIL.getValue(), null, result.id);
                }
            }

            offset += limit;
        }
    }

}
