package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "/weixin")
public class WeixinController extends BaseController {



    static final Logger log = LogManager.getLogger(WeixinController.class);
    @NotLogin
    @RequestMapping(value = "/get_open_id.htm")
    public void getOpenId(int appId, String redirectUri, String signature, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!CodecUtils.md5(appId + "|" + redirectUri + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK").equals(signature)) {
            return;
        }

        //https://zudian.yusong.com.cn/battery_qrcode/index_get_openid.htm?v=Z0000020&signature=8865B4BB68D5124B0F59BF861484A983
        if (StringUtils.isNotEmpty(redirectUri) && redirectUri.contains("https://zudian.yusong.com.cn/battery_qrcode/index_get_openid.htm?v=")) {
            String v = StringUtils.substringBetween(redirectUri, "v=", "&");
            if (StringUtils.isNotEmpty(v) && v.length() > 2) {
                String batteryId = "ZZ" + v.substring(2);
                String url = String.format("%s/battery_qrcode/index.htm?v=%s", appConfig.domainUrl, batteryId);
                response.sendRedirect(url);
                return;
            }
        }

        if(appId == 0){
            appId = 1;
        }
        Weixinmp weixinmp = weixinmpService.find(appId);

        String wxmpAppId = weixinmp.getAppId();


        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                wxmpAppId,
                AppUtils.encodeUrl(String.format("%s/%s/%d/sns_api_base.htm", appConfig.getDomainUrl(), "weixin", appId), "UTF-8"),
                redirectUri);
        response.setStatus(302);
        response.setHeader("Location", url);
    }
    @NotLogin
    @RequestMapping(value = "/{appId}/sns_api_base.htm")
    public void mpSnsApiBase(@PathVariable("appId") int appId, String code, String state, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("code = {}, state = {}", code, state); //state是充电桩点的二维码
        }
        Weixinmp weixinmp = weixinmpService.find(appId);

        WxMpUser wxMpUser = null;
        WxMpOAuth2AccessToken accessToken = null;
        WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(weixinmp.getId());
        if (wxMpService == null) {
            log.error(String.format("wxMpService not exist, id=%d", weixinmp.getId()));
        }

        for (int i = 0; i < 10; i++) {
            try {
                accessToken = wxMpService.oauth2getAccessToken(code);
                if (log.isDebugEnabled()) {
                    log.debug("accessToken = {}", accessToken);
                }
                wxMpUser = wxMpService.oauth2getUserInfo(accessToken, "zh_CN");
                break;
            } catch (WxErrorException wxError) {
                log.error("oauth2getAccessToken error", wxError);
                break;
            } catch (Exception e) {
                log.error("oauth2getAccessToken error", e);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

        if (accessToken != null) {

            String openId = accessToken.getOpenId();
            String url = state;
            String signature = CodecUtils.md5(wxMpService.getWxMpConfigStorage().getAppId() + "|" + state + "|" + openId + "|" + DateFormatUtils.format(new Date(), Constant.DATE_FORMAT) + "|" + "YSKJ-ERTYUIOFGHJKLRTYUIOUIRTYUIOYUIOFGHJK");
            if (url.indexOf("?") == -1) {
                url += "?openId=" + openId;
            } else {
                url += "&openId=" + openId;
            }
            if (wxMpUser != null) {
                url += "&photoPath=" + AppUtils.encodeUrl(wxMpUser.getHeadImgUrl(), "UTF-8");
                url += "&nickname=" + AppUtils.encodeUrl(filterEmoji(wxMpUser.getNickname()), "UTF-8");

//                try {
//                    //是否关注公众号
//                    wxMpUser = wxMpService.getUserService().userInfo(openId, null);
//                    if(wxMpUser != null){
//                        url += "&subscribed=" + wxMpUser.getSubscribe();
//                    }
//                } catch (WxErrorException e) {
//                    e.printStackTrace();
//                }
            }
            url += "&wxAppId=" + wxMpService.getWxMpConfigStorage().getAppId();
            url += "&signature=" + signature;


            response.sendRedirect(url);
        }
    }
}
