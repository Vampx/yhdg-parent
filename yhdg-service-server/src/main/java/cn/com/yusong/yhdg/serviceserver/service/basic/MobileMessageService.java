package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.MobileMessageMapper;
import cn.com.yusong.yhdg.serviceserver.tool.sms.Param;
import cn.com.yusong.yhdg.serviceserver.tool.sms.Result;
import cn.com.yusong.yhdg.serviceserver.tool.sms.SmsHttpClientManager;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class MobileMessageService {
    static Logger log = LogManager.getLogger(MobileMessageService.class);

    @Autowired
    MobileMessageMapper mobileMessageMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;

    public int insert(MobileMessage entity) {
        return mobileMessageMapper.insert(entity);
    }

    @Transactional(rollbackFor=Throwable.class)
    public void scanMessage() {
        int offset = 0, limit = 100;

        while (true) {
            List<MobileMessage> messageList = mobileMessageMapper.findList(MobileMessage.MessageStatus.NOT.getValue(), offset, limit);
            if(messageList.isEmpty()) {
                break;
            }

            for(MobileMessage message : messageList) {
                if(message.getDelay() == 0 || System.currentTimeMillis() - message.getCreateTime().getTime() >= message.getDelay() * 1000) {
                    String key = CacheKey.key(CacheKey.K_SMS_MOBILE_V_ZERO, message.getMobile());
                    if(memCachedClient.get(key) == null) {
                        Param param = new Param();
                        param.partnerId = message.getPartnerId();
                        param.id = message.getId();
                        param.mobile = message.getMobile();
                        param.content = message.getContent();
                        param.vairable = message.getVariable();
                        param.templateCode = message.getTemplateCode();
                        Result result = config.sms.send(param);
                        if(result != null) {
                            if(result.success) {
                                memCachedClient.set(key, 0, MemCachedConfig.CACHE_ONE_MINUTE);
                                mobileMessageMapper.complete(message.getId(), MobileMessage.MessageStatus.OK.getValue(), new Date(), result.id);
                                mobileMessageMapper.updateMsgId(message.getId(), result.msgId);
                            } else {
                                mobileMessageMapper.complete(message.getId(), MobileMessage.MessageStatus.FAIL.getValue(), null, result.id);
                            }
                        } else {
                            log.error("Not available SmsConfig, partnerId = {}", param.partnerId);
                        }
                    }
                }
            }

            offset += limit;
        }
    }

    public void cleanMessage() {
        List<Integer> sourceType = new LinkedList<Integer>();

        sourceType.add(MobileMessage.SourceType.AUTH_CODE.getValue());
        sourceType.add(MobileMessage.SourceType.AUTH_CODE.getValue());

        Date createTime = DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), -7); //一周之前的数据
        List<Long> list = mobileMessageMapper.findByType(sourceType, createTime, 100);

        for(Long id : list) {
            mobileMessageMapper.delete(id);
        }
    }
}
