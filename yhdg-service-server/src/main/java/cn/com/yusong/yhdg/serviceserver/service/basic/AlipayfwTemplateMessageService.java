package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwTemplateMessage;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.AlipayfwTemplateMessageMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 生活号模板消息服务
 */
@Service
public class AlipayfwTemplateMessageService extends AbstractService {

    private static Logger log = LogManager.getLogger(AlipayfwTemplateMessageService.class);

    @Autowired
    private AlipayfwTemplateMessageMapper alipayfwTemplateMessageMapper;


    private Sender defaultSender = new Sender() {
        @Override
        public AlipayOpenPublicMessageSingleSendResponse doSend(AlipayfwClientHolder alipayfwClientHolder, String domainUrl, Map<Integer, Map<String, String>> colorsMap, AlipayfwTemplateMessage message) throws IOException, AlipayApiException {
            return AlipayfwTemplateMessageService.this.doSend(alipayfwClientHolder, domainUrl, colorsMap, message);
        }
    };

    /**
     * 数据库查询条数
     */
    private static final int limit = 10;

    public void sendMessage(AlipayfwClientHolder alipayfwClientHolder, Sender sender, String domainUrl) throws IOException {
        if(sender == null) {
            sender = defaultSender;
        }

        Map<Integer, Map<String, String>> colorsMap = new HashMap<Integer, Map<String, String>>();
        Long lastId = null; //查询出来的最后一个ID

        while (true) {
            //查询
            List<AlipayfwTemplateMessage> messageList = alipayfwTemplateMessageMapper.findListByStatus(AlipayfwTemplateMessage.MessageStatus.NOT.getValue(), limit, lastId);
            //没有结果,直接返回
            if (messageList == null || messageList.isEmpty()) break;
            lastId = messageList.get(messageList.size() - 1).getId();
            for (AlipayfwTemplateMessage message : messageList) {
                if (doCheckMessage(message)) {//校验消息
                    //发送消息
                    try {
                        AlipayOpenPublicMessageSingleSendResponse response = sender.doSend(alipayfwClientHolder, domainUrl, colorsMap, message);
                        if(response != null) {
                            if(response.isSuccess()) {
                                alipayfwTemplateMessageMapper.complete(message.getId(), new Date(), AlipayfwTemplateMessage.MessageStatus.OK.getValue(), message.getResendNum());
                            } else {
                                log.error("alipay template message {} fail: {}", message.getId(), response.getBody());
                                throw new IllegalStateException("response fail");
                            }
                        } else {
                            throw new IllegalStateException("response is null");
                        }

                    } catch (Exception e) {
                        log.error("alipay send template error, messageId:" + message.getId(), e);
                        doSendFail(message); //发送失败处理
                    }
                }
            }
            //如果查询出来的条数小于100条，处理完后直接跳出，不再进行下一次查询
            if (messageList.size() < limit) break;
        }
    }

    /**
     * 消息发送失败处理
     *
     * @param message
     */
    private void doSendFail(AlipayfwTemplateMessage message) {
        //如果发送次数超过10次，直接失败
        if (message.getResendNum() != null && message.getResendNum() > 2) {
            alipayfwTemplateMessageMapper.complete(message.getId(), new Date(), AlipayfwTemplateMessage.MessageStatus.FAIL.getValue(), message.getResendNum());
            return;
        }
        //发送次数加1
        alipayfwTemplateMessageMapper.complete(message.getId(), new Date(), AlipayfwTemplateMessage.MessageStatus.NOT.getValue(), message.getResendNum() == null ? 1 : message.getResendNum() + 1);
    }

    /**
     * 校验消息
     *
     * @param message
     * @return
     */
    private boolean doCheckMessage(AlipayfwTemplateMessage message) {
        //校验OpenId
        if (StringUtils.isEmpty(message.getOpenId())) return false;

        FwPushMessageTemplate pushMessageTemplate = findFwPushMessageTemplate(message.getAlipayfwId(), message.getType());
        if (pushMessageTemplate != null && pushMessageTemplate.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            message.setStatus(WeixinmpTemplateMessage.MessageStatus.DISABLED.getValue());
            alipayfwTemplateMessageMapper.updateStatus(message.getStatus(), message.getId(), new Date());
            return false;
        }

        //校验时间
        if (message.getDelay() != 0 && (System.currentTimeMillis() - message.getCreateTime().getTime() < message.getDelay() * 1000))
            return false;

        return true;

    }

    public AlipayOpenPublicMessageSingleSendResponse doSend(AlipayfwClientHolder alipayfwClientHolder, String domainUrl, Map<Integer, Map<String, String>> colorsMap, AlipayfwTemplateMessage message) throws AlipayApiException, IOException {

        FwPushMessageTemplate pushMessageTemplate = findFwPushMessageTemplate(message.getAlipayfwId(), message.getType());

        AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();

        Map<String, Object> bizContent = new HashMap<String, Object>();
        bizContent.put("to_user_id", message.getOpenId());

        Map<String, Object> template = new HashMap<String, Object>();
        bizContent.put("template", template);

        template.put("template_id", pushMessageTemplate.getFwCode());

        Map<String, Object> context = new HashMap<String, Object>();
        template.put("context", context);

        context.put("head_color", "#000000");

        if(StringUtils.isNotEmpty(message.getUrl())) {
            message.setUrl(message.getUrl().replace("${messageId}", String.format("%d", message.getId())));

            if(message.getUrl().startsWith("http://") || message.getUrl().startsWith("https://")) {
                context.put("url", message.getUrl());
            } else {
                context.put("url", domainUrl + message.getUrl());
            }
        } else {
            context.put("url", "");
        }


        context.put("action_name", "");

        Map<String, String> param = (Map<String, String>) AppUtils.decodeJson(message.getVariable(), Map.class);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            Map attribute = new HashMap();
            attribute.put("color", pushMessageTemplate.colorMap.get(entry.getKey()));
            attribute.put("value", entry.getValue());
            context.put(entry.getKey(), attribute);

            if(StringUtils.isEmpty(entry.getValue())) {
                log.error("模板消息{} {}对应的值是空", message.getId(), entry.getKey());
            }
            if(entry.getValue().contains("${")) {
                log.error("模板消息{} key:{} value:{}包含变量", message.getId(), entry.getKey(), entry.getValue());
            }
        }

        request.setBizContent(AppUtils.encodeJson(bizContent));
        AlipayClient alipayClient = alipayfwClientHolder.getAlipayfw(message.getAlipayfwId());
        if(alipayClient == null) {
            throw new IllegalArgumentException(String.format("AlipayfwClientHolder appId=%d 没有设置", message.getAlipayfwId()));
        }

        AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request);
        if(response != null) {
            if(response.isSuccess()) {
                if(log.isDebugEnabled()) {
                    log.debug("模板消息成功: {}, {}", message.getId(), response.getBody());
                }
            } else {
                log.error("模板消息失败: {}, {}", message.getId(), response.getBody());
                throw new IllegalStateException("模板消息失败");
            }
        } else {
            log.error("模板消息 {} 失败: response是null", message.getId());
        }
        return response;
    }

    public static interface Sender {
        public AlipayOpenPublicMessageSingleSendResponse doSend(AlipayfwClientHolder alipayfwClientHolder, String domainUrl, Map<Integer, Map<String, String>> colorsMap, AlipayfwTemplateMessage message) throws IOException, AlipayApiException;
    }

}
