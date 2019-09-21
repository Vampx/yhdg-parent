package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BaseController extends SuperController {

    private final static Logger log = LogManager.getLogger(BaseController.class);

    public String appendAppId(String url, int appId) {
        url += url.contains("?") ? "&" : "?";
        url += String.format("appId=%d", appId);
        return url;
    }

    public boolean isMpSubscribed(Weixinmp weixinmp, String openId) {
        boolean subscribed = false;
        try {
            WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(weixinmp.getId());
            if (wxMpService == null) {
                log.error(String.format("wxMpService not exist, id=%d", Constant.SYSTEM_PARTNER_ID));
            }

            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId, null);
            subscribed = wxMpUser.getSubscribe();
        } catch (Exception e) {
            log.error("openId {} userInfo error", openId);
            log.error("userInfo error", e);
            //这里可能会报错 如果报错了 就直接跳过
            return true;
        }
        if (subscribed) {
            mpSubscribeService.subscribe(weixinmp.getId(), openId);
        } else {
            mpSubscribeService.unsubscribe(weixinmp.getId(), openId);
        }

        return subscribed;
    }

    public boolean isFwSubscribed(Alipayfw alipayfw, String openId) {
        AlipayfwSubscribe subscribe = fwSubscribeService.findByOpenId(alipayfw.getId(), openId);
        return subscribe != null;
    }

    public static String filterEmoji(String source) {
        if (StringUtils.isNotEmpty(source)) {
            return source.replaceAll("[\\x{10000}-\\x{10FFFF}]", "");
        }
        return source;
    }

    protected void acquireOpenId(String qrcode, String openId) {

    }
}
