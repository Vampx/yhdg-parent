package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.WeixinmpTemplateMessageMapper;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信模板消息服务
 */
@Service
public class WeixinmpTemplateMessageService extends AbstractService {

    private static Logger log = LogManager.getLogger(WeixinmpTemplateMessageService.class);

    @Autowired
    WeixinmpTemplateMessageMapper weixinmpTemplateMessageMapper;

    private Sender defaultSender = new Sender() {
        @Override
        public void doSend(WxMpServiceHolder wxMpServiceHolder, String weixinMsgUrl, Map<Integer, Map<String, String>> colorsMap, WeixinmpTemplateMessage message) throws WxErrorException, IOException {
            WeixinmpTemplateMessageService.this.doSend(wxMpServiceHolder, weixinMsgUrl, colorsMap, message);
        }
    };

    /**
     * 数据库查询条数
     */
    private static final int limit = 10;

    public void sendMessage(WxMpServiceHolder wxMpService, Sender sender, String domainUrl) throws IOException {
        if(sender == null) {
            sender = defaultSender;
        }

        Map<Integer, Map<String, String>> colorsMap = new HashMap<Integer, Map<String, String>>();
        Long lastId = null; //查询出来的最后一个ID

        while (true) {

            if(log.isDebugEnabled()) {
                log.debug("findListByStatus begin");
            }

            //查询
            List<WeixinmpTemplateMessage> messageList = weixinmpTemplateMessageMapper.findListByStatus(WeixinmpTemplateMessage.MessageStatus.NOT.getValue(), limit, lastId);

            if(log.isDebugEnabled()) {
                log.debug("findListByStatus end");
            }


            //没有结果,直接返回
            if (messageList == null || messageList.isEmpty()) break;
            lastId = messageList.get(messageList.size() - 1).getId();
            for (WeixinmpTemplateMessage message : messageList) {

                if(log.isDebugEnabled()) {
                    log.debug("message {}", message.getId());
                }


                //发送消息
                try {
                    if (doCheckMessage(message)) { //校验消息

                        if(log.isDebugEnabled()) {
                            log.debug("check after message {}", message.getId());
                        }

                        if(log.isDebugEnabled()) {
                            log.debug("modifyMessage after message {}", message.getId());
                        }

                        sender.doSend(wxMpService, domainUrl, colorsMap, message);

                        if(log.isDebugEnabled()) {
                            log.debug("doSend after message {}", message.getId());
                        }

                        weixinmpTemplateMessageMapper.complete(message.getId(), new Date(), WeixinmpTemplateMessage.MessageStatus.OK.getValue(), message.getResendNum());

                        if(log.isDebugEnabled()) {
                            log.debug("complete after message {}", message.getId());
                        }
                    }
                } catch (WxErrorException e) {
                    log.error("send template error,messageId:" + message.getId(), e);
                    doSendFail(message); //发送失败处理
                    // 如果没有关注公众号 提示{"errcode":43004,"errmsg":"require subscribe hint: [pbUIDa0472ge21]"}

                } catch (Exception e) {
                    //数据格式错误
                    doSendFail(message); //发送失败处理
                    log.error("weixinMsg decodeJson error,messageId:" + message.getId(), e);
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
    private void doSendFail(WeixinmpTemplateMessage message) {
        //如果发送次数超过10次，直接失败
        //if (message.getResendNum() != null && message.getResendNum() > 2) {
            weixinmpTemplateMessageMapper.complete(message.getId(), new Date(), WeixinmpTemplateMessage.MessageStatus.FAIL.getValue(), message.getResendNum());
            //return;
        //}
        //发送次数加1
        //weixinmpTemplateMessageMapper.complete(message.getId(), new Date(), WeixinmpTemplateMessage.MessageStatus.NOT.getValue(), message.getResendNum() == null ? 1 : message.getResendNum() + 1);

    }

    /**
     * 校验消息
     *
     * @param message
     * @return
     */
    private boolean doCheckMessage(WeixinmpTemplateMessage message) {
        //校验OpenId
        if (StringUtils.isEmpty(message.getOpenId())) return false;

        MpPushMessageTemplate pushMessageTemplate = findMpPushMessageTemplate(message.getWeixinmpId(), message.getType());
        if (pushMessageTemplate != null && pushMessageTemplate.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            message.setStatus(WeixinmpTemplateMessage.MessageStatus.DISABLED.getValue());
            weixinmpTemplateMessageMapper.updateStatus(message.getStatus(), message.getId(), new Date());
            return false;
        }

        //校验时间
        if (message.getDelay() != 0 && (System.currentTimeMillis() - message.getCreateTime().getTime() < message.getDelay() * 1000))
            return false;

        return true;

    }

    public void doSend(WxMpServiceHolder wxMpServiceHolder, String domainUrl, Map<Integer, Map<String, String>> colorsMap, WeixinmpTemplateMessage message) throws WxErrorException, IOException {

        if(log.isDebugEnabled()) {
            log.debug("appId====> [{}]", message.getWeixinmpId());
            log.debug("Id====> [{}]", message.getType());
        }

        MpPushMessageTemplate pushMessageTemplate = findMpPushMessageTemplate(message.getWeixinmpId(), message.getType());

        if(log.isDebugEnabled()) {
            log.debug("templateId====> [{}]", pushMessageTemplate.getMpCode());
        }

        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setToUser(message.getOpenId());
        wxMpTemplateMessage.setTemplateId(pushMessageTemplate.getMpCode());

        if(StringUtils.isNotEmpty(message.getUrl())) {
            message.setUrl(message.getUrl().replace("${messageId}", String.format("%d", message.getId())));
            if(message.getUrl().startsWith("http://") || message.getUrl().startsWith("https://")) {
                wxMpTemplateMessage.setUrl(message.getUrl());
            } else {
                wxMpTemplateMessage.setUrl(domainUrl + message.getUrl());
            }

        }

        Map<String, String> param = (Map<String, String>) AppUtils.decodeJson(message.getVariable(), Map.class);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            WxMpTemplateData wxMpTemplateData =  new WxMpTemplateData(entry.getKey(), entry.getValue(), pushMessageTemplate.colorMap.get(entry.getKey()));
            wxMpTemplateMessage.addData(wxMpTemplateData);

            if(StringUtils.isEmpty(entry.getValue())) {
                log.error("模板消息{} {}对应的值是空", message.getId(), entry.getKey());
            }
            if(entry.getValue().contains("${")) {
                log.error("模板消息{} key:{} value:{}包含变量", message.getId(), entry.getKey(), entry.getValue());
            }
        }

        if(log.isDebugEnabled()) {
            log.debug("appId====> [{}]", message.getWeixinmpId());
        }
        WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(message.getWeixinmpId());
        if(wxMpService == null) {
            throw new IllegalArgumentException("appId=%d对应的WxMpService是空");
        }

        if(log.isDebugEnabled()) {
            log.debug("templateSend====> [{}]", message.getId());
        }
        //发送给微信服务器
        wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);

        if(log.isDebugEnabled()) {
            log.debug("templateSend after ====> [{}]", message.getId());
        }
    }

    public static interface Sender {
        public void doSend(WxMpServiceHolder wxMpServiceHolder, String domainUrl, Map<Integer, Map<String, String>> colorsMap, WeixinmpTemplateMessage message) throws WxErrorException, IOException;
    }
}
