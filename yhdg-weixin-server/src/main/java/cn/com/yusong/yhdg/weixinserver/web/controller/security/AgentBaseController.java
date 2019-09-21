package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
import cn.com.yusong.yhdg.weixinserver.utils.DownloadFileUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AgentBaseController extends BaseController {

    private final static Logger log = LogManager.getLogger(BaseController.class);

    public void systemMpIndex(int appId, String targetUrl, String basePath, String qrcode, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Weixinmp weixinmp = weixinmpService.find(appId);
        if (weixinmp == null) {
            responseText(response, "公众号配置不存在");
            return;
        }
        Partner partner = null;
        if (weixinmp.getPartnerId() == null || (partner = partnerService.find(weixinmp.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (weixinmp.getPageType() == Weixinmp.PageType.DEFAULT.getValue()) {
            writeCookie(0, response);
        } else {
            writeCookie(weixinmp.getId(), response);
        }


        targetUrl = getAppHtmlPath(weixinmp.getId(), targetUrl);
        qrcode = AppUtils.encodeUrl(StringUtils.trimToEmpty(qrcode), "UTF-8");
        SessionUser sessionUser = getSessionUser(httpSession);
        if (sessionUser != null
                && StringUtils.isNotEmpty(sessionUser.getOpenId())
                && partnerMpOpenIdService.findByOpenId(partner.getId(), sessionUser.getOpenId()) != null
                && weixinmpOpenIdService.findByOpenId(weixinmp.getId(), sessionUser.getOpenId()) != null
                && sessionUser.getAppId() == weixinmp.getId()) {
            if (forceSubscribe() && !isMpSubscribed(weixinmp, sessionUser.getSecondOpenId())) {
                String wxSubscribeUrl = weixinmp.getSubscribeUrl();

                if(StringUtils.isNotEmpty(wxSubscribeUrl)) {
                    response.sendRedirect(appendAppId(wxSubscribeUrl, appId));
                    return;
                }

            }
            targetUrl += targetUrl.contains("?") ? "&" : "?";
            String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), qrcode, weixinmp.getId());
            response.sendRedirect(url = buildRedirectUrl(url, sessionUser, qrcode));

            if(log.isDebugEnabled()) {
                log.debug("SessionUser: {}", AppUtils.encodeJson(sessionUser));
                log.debug("systemMpIndex_1 redirect url: {}", url);
            }

        } else {
            String wxAppId = partner.getMpAppId();
            if(StringUtils.isNotEmpty(wxAppId)) {
                String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect",
                        wxAppId,
                        AppUtils.encodeUrl(String.format("%s/%s/system_sns_api_base_%d.htm", appConfig.getDomainUrl(), basePath, weixinmp.getId()), "UTF-8"),
                        qrcode);
                response.setStatus(302);
                response.setHeader("Location", url);
                if (log.isDebugEnabled()) {
                    log.debug("systemMpIndex_2 redirectUrl: {}", url);
                }

            } else {
                log.error("WxAppId is empty, appId: {}", Constant.SYSTEM_PARTNER_ID);
            }

        }
    }

    public void systemFwIndex(int appId, String targetUrl, String basePath, String qrcode, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Alipayfw alipayfw = alipayfwService.find(appId);
        if (alipayfw == null) {
            responseText(response, "生活号配置不存在");
            return;
        }
        Partner partner = null;
        if (alipayfw.getPartnerId() == null || (partner = partnerService.find(alipayfw.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (alipayfw.getPageType() == Alipayfw.PageType.DEFAULT.getValue()) {
            writeCookie(0, response);
        } else {
            writeCookie(alipayfw.getId(), response);
        }


        targetUrl = getAppHtmlPath(alipayfw.getId(), targetUrl);
        qrcode = AppUtils.encodeUrl(StringUtils.trimToEmpty(qrcode), "UTF-8");
        SessionUser sessionUser = getSessionUser(httpSession);
        if (sessionUser != null
                && StringUtils.isNotEmpty(sessionUser.getOpenId())
                && alipayfwOpenIdService.findByOpenId(partner.getId(), sessionUser.getOpenId()) != null
                && alipayfwOpenIdService.findByOpenId(alipayfw.getId(), sessionUser.getOpenId()) != null
                && sessionUser.getAppId() == alipayfw.getId()) {
            if (forceSubscribe() && !isFwSubscribed(alipayfw, sessionUser.getSecondOpenId())) {
                String fwSubscribeUrl = alipayfw.getSubscribeUrl();

                if(StringUtils.isNotEmpty(fwSubscribeUrl)) {
                    response.sendRedirect(appendAppId(fwSubscribeUrl, appId));
                    return;
                }

            }
            targetUrl += targetUrl.contains("?") ? "&" : "?";
            String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), qrcode, alipayfw.getId());
            response.sendRedirect(url = buildRedirectUrl(url, sessionUser, qrcode));

            if(log.isDebugEnabled()) {
                log.debug("SessionUser: {}", AppUtils.encodeJson(sessionUser));
                log.debug("systemFwIndex_1 redirect url: {}", url);
            }

        } else {
            String fwAppId = partner.getFwAppId();
            if(StringUtils.isNotEmpty(fwAppId)) {
                String url = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_base&redirect_uri=%s&state=%s",
                        fwAppId,
                        AppUtils.encodeUrl(String.format("%s/%s/system_sns_api_base_%d.htm", appConfig.getDomainUrl(), basePath, alipayfw.getId()), "UTF-8"),
                        qrcode);
                response.setStatus(302);
                response.setHeader("Location", url);
                if (log.isDebugEnabled()) {
                    log.debug("systemFwIndex_2 redirectUrl: {}", url);
                }

            } else {
                log.error("systemFwIndex_2 fwAppId is empty, appId: {}", Constant.SYSTEM_PARTNER_ID);
            }

        }
    }

    public void systemMpSnsApiBase(int appId, String code, String state, String targetUrl, String basePath, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Weixinmp weixinmp = weixinmpService.find(appId);
        if (weixinmp == null) {
            responseText(response, "公众号配置不存在");
            return;
        }
        Partner partner = null;
        if (weixinmp.getPartnerId() == null || (partner = partnerService.find(weixinmp.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("systemMpSnsApiBase appId = {}, code = {}, state = {}", Constant.SYSTEM_PARTNER_ID, code, state);
        }
        targetUrl = getAppHtmlPath(weixinmp.getId(), targetUrl);
        state = AppUtils.encodeUrl(StringUtils.trimToEmpty(state), "UTF-8");

        WxMpOAuth2AccessToken accessToken = null;
        WxMpService wxMpService = wxMpServiceHolder.getPartner(partner.getId());
        if (wxMpService == null) {
            log.error(String.format("wxMpService not exist, id=%d", Constant.SYSTEM_PARTNER_ID));
        }

        for (int i = 0; i < 10; i++) {
            try {
                accessToken = wxMpService.oauth2getAccessToken(code);
                if (log.isDebugEnabled()) {
                    log.debug("systemMpSnsApiBase accessToken = {}", accessToken);
                }
                break;
            } catch (WxErrorException wxError) {
                log.error("systemMpSnsApiBase oauth2getAccessToken error", wxError);
                break;
            } catch (Exception e) {
                log.error("systemMpSnsApiBase oauth2getAccessToken error", e);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

        if (accessToken != null) {

            String weixinOpenId = accessToken.getOpenId();
            PartnerMpOpenId openId = partnerMpOpenIdService.findByOpenId(partner.getId(), weixinOpenId);
            if (openId == null) {
                openId = new PartnerMpOpenId();
                openId.setPartnerId(partner.getId());
                openId.setOpenId(weixinOpenId);
                openId.setCreateTime(new Date());
                partnerMpOpenIdService.insert(openId);

                String wxAppId = weixinmp.getAppId();
                if(StringUtils.isNotEmpty(wxAppId)) {
                    String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                            wxAppId,
                            AppUtils.encodeUrl(String.format("%s/%s/%s/agent_sns_api_user_info_%d.htm", appConfig.getDomainUrl(), basePath, weixinOpenId, weixinmp.getId() ), "UTF-8"),
                            state);
                    response.setStatus(302);
                    response.setHeader("Location", url);
                    if (log.isDebugEnabled()) {
                        log.debug("systemMpSnsApiBase redirectUrl: {}", url);
                    }

                } else {
                    log.error("systemMpSnsApiBase WxAppId is empty, appId: {}", Constant.SYSTEM_PARTNER_ID);
                }
            } else {
                WeixinmpOpenId weixinmpOpenId = weixinmpOpenIdService.findByOpenId(weixinmp.getId(), weixinOpenId);
                if(weixinmpOpenId == null) {

                    String wxAppId = weixinmp.getAppId();
                    if(StringUtils.isNotEmpty(wxAppId)) {
                        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                                wxAppId,
                                AppUtils.encodeUrl(String.format("%s/%s/%s/agent_sns_api_user_info_%d.htm", appConfig.getDomainUrl(), basePath, weixinOpenId, weixinmp.getId()), "UTF-8"),
                                state);
                        response.setStatus(302);
                        response.setHeader("Location", url);
                        if (log.isDebugEnabled()) {
                            log.debug("systemMpSnsApiBase redirectUrl: {}", url);
                        }

                    } else {
                        log.error("systemMpSnsApiBase WxAppId is empty, appId: {}", Constant.SYSTEM_PARTNER_ID);
                    }

                } else {
                    SessionUser sessionUser = new SessionUser();
                    sessionUser.setAppId(weixinmp.getId());
                    sessionUser.setOpenId(weixinOpenId);
                    sessionUser.setSecondOpenId(weixinmpOpenId.getSecondOpenId());
                    httpSession.setAttribute(AppConstant.SESSION_KEY_USER, sessionUser);

                    if (forceSubscribe() && !isMpSubscribed(weixinmp, sessionUser.getSecondOpenId())) {
                        String wxSubscribeUrl = weixinmp.getSubscribeUrl();

                        if(StringUtils.isNotEmpty(wxSubscribeUrl)) {
                            response.sendRedirect(appendAppId(wxSubscribeUrl, appId));
                            return;
                        }

                    }
                    targetUrl += targetUrl.contains("?") ? "&" : "?";
                    String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), state, weixinmp.getId());
                    url = buildRedirectUrl(url, sessionUser, state);
                    if(log.isDebugEnabled()) {
                        log.debug("systemMpSnsApiBase redirect url : {}", url);
                    }

                    response.sendRedirect(url);
                }

            }
        }
    }

    public void systemFwSnsApiBase(int appId, String auth_code, String state, String targetUrl, String basePath, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Alipayfw alipayfw = alipayfwService.find(appId);
        if (alipayfw == null) {
            responseText(response, "公众号配置不存在");
            return;
        }
        Partner partner = null;
        if (alipayfw.getPartnerId() == null || (partner = partnerService.find(alipayfw.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("systemFwSnsApiBase appId = {}, auth_code = {}, state = {}", Constant.SYSTEM_PARTNER_ID, auth_code, state);
        }
        targetUrl = getAppHtmlPath(alipayfw.getId(), targetUrl);
        state = AppUtils.encodeUrl(StringUtils.trimToEmpty(state), "UTF-8");

        try {
            AlipayClient alipayClient = alipayClientHolder.getPartner(partner.getId());

            //3. 利用authCode获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(auth_code);
            oauthTokenRequest.setGrantType("authorization_code");
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);

            //成功获得authToken
            if (oauthTokenResponse != null && oauthTokenResponse.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("getOpenId, {}", oauthTokenResponse.getBody());
                }

                PartnerFwOpenId openId = partnerFwOpenIdService.findByOpenId(partner.getId(), oauthTokenResponse.getUserId());
                if (openId == null) {
                    openId = new PartnerFwOpenId();
                    openId.setPartnerId(partner.getId());
                    openId.setOpenId(oauthTokenResponse.getUserId());
                    openId.setCreateTime(new Date());
                    partnerFwOpenIdService.insert(openId);

                    String fwAppId = alipayfw.getAppId();
                    int fwUserinfoVersion = alipayfw.getUserinfoVersion();
                    String scope = fwUserinfoVersion == 1 ? "auth_userinfo" : "auth_user";

                    /*如果scope参数是auth_userinfo，那么可能是ACCESSTOKEN过期了，建议重新授权获取一次 scope=auth_userinfo*/
                    String url = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=%s&redirect_uri=%s&state=%s",
                            fwAppId,
                            scope,
                            AppUtils.encodeUrl(String.format("%s/%s/%s/agent_sns_api_user_info_%d.htm", appConfig.getDomainUrl(), basePath, oauthTokenResponse.getUserId(), alipayfw.getId()), "UTF-8"),
                            state);

                    if (log.isDebugEnabled()) {
                        log.debug("systemFwSnsApiBase redirectUrl: {}", url);
                    }

                    response.setStatus(302);
                    response.setHeader("Location", url);


                } else {
                    AlipayfwOpenId agentFwOpenId = alipayfwOpenIdService.findByOpenId(alipayfw.getId(), oauthTokenResponse.getUserId());
                    if(agentFwOpenId == null) {
                        String fwAppId = alipayfw.getAppId();
                        int fwUserinfoVersion = alipayfw.getUserinfoVersion();
                        String scope = fwUserinfoVersion == 1 ? "auth_userinfo" : "auth_user";

                        if(StringUtils.isNotEmpty(fwAppId)) {
                            /*如果scope参数是auth_userinfo，那么可能是ACCESSTOKEN过期了，建议重新授权获取一次 scope=auth_userinfo*/
                            String url = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=%s&redirect_uri=%s&state=%s",
                                    fwAppId,
                                    scope,
                                    AppUtils.encodeUrl(String.format("%s/%s/%s/agent_sns_api_user_info_%d.htm", appConfig.getDomainUrl(), basePath, oauthTokenResponse.getUserId(), alipayfw.getId()), "UTF-8"),
                                    state);
                            response.setStatus(302);
                            response.setHeader("Location", url);
                            if (log.isDebugEnabled()) {
                                log.debug("systemFwSnsApiBase redirectUrl: {}", url);
                            }

                        } else {
                            log.error("systemFwSnsApiBase FwAppId is empty, appId: {}", Constant.SYSTEM_PARTNER_ID);
                        }

                    } else {
                        SessionUser sessionUser = new SessionUser();
                        sessionUser.setAppId(alipayfw.getId());
                        sessionUser.setOpenId(oauthTokenResponse.getUserId());
                        sessionUser.setSecondOpenId(agentFwOpenId.getSecondOpenId());
                        httpSession.setAttribute(AppConstant.SESSION_KEY_USER, sessionUser);

                        if (forceSubscribe() && !isFwSubscribed(alipayfw, sessionUser.getSecondOpenId())) {
                            String wxSubscribeUrl = alipayfw.getSubscribeUrl();

                            if(StringUtils.isNotEmpty(wxSubscribeUrl)) {
                                response.sendRedirect(appendAppId(wxSubscribeUrl, appId));
                                return;
                            }

                        }
                        targetUrl += targetUrl.contains("?") ? "&" : "?";
                        String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), state, alipayfw.getId());
                        url = buildRedirectUrl(url, sessionUser, state);
                        if(log.isDebugEnabled()) {
                            log.debug("systemFwSnsApiBase redirect url : {}", url);
                        }

                        response.sendRedirect(url);
                    }
                }

            } else {
                log.error("systemFwSnsApiBase getOpenId fail, {}", oauthTokenResponse.getBody());
            }

        } catch (AlipayApiException alipayApiException) {
            log.error("systemFwSnsApiBase AlipaySystemOauthTokenRequest error", alipayApiException);
        }
    }

    public void agentMpSnsApiUserInfo(int appId, String systemOpenId, String code, String state, String targetUrl, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Weixinmp weixinmp = weixinmpService.find(appId);
        if (weixinmp == null) {
            responseText(response, "公众号配置不存在");
            return;
        }
        Partner partner = null;
        if (weixinmp.getPartnerId() == null || (partner = partnerService.find(weixinmp.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("agentMpSnsApiUserInfo appId = {}, code = {}, state = {}", weixinmp.getId(), code, state); //state是充电桩点的二维码
        }
        targetUrl = getAppHtmlPath(weixinmp.getId(), targetUrl);
        state = AppUtils.encodeUrl(StringUtils.trimToEmpty(state), "UTF-8");

        WxMpUser wxMpUser = null;
        String weixinOpenId = null;
        WxMpService wxMpService = wxMpServiceHolder.getWeixinmp(weixinmp.getId());
        if (wxMpService == null) {
            log.error(String.format("agentMpSnsApiUserInfo wxMpService not exist, id=%d", weixinmp.getId()));
        }

        for (int i = 0; i < 100; i++) {
            try {
                WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(code);
                if (log.isDebugEnabled()) {
                    log.debug("agentMpSnsApiUserInfo accessToken = {}", accessToken);
                }

                weixinOpenId = accessToken.getOpenId();
                wxMpUser = wxMpService.oauth2getUserInfo(accessToken, "zh_CN");
                break;
            } catch (Exception e) {
                log.error(e);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }

        if (wxMpUser != null) {
            String photoPath = null;

            if (StringUtils.isNotEmpty(wxMpUser.getHeadImgUrl())) {
                File tempFile = new File(appConfig.tempDir, IdUtils.uuid() + ".jpg");
                DownloadFileUtils.download(wxMpUser.getHeadImgUrl(), tempFile);
                if (tempFile.exists()) {
                    Map<String, File> fileMap = new HashMap<String, File>();
                    fileMap.put(tempFile.getName(), tempFile);

                    HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_PHOTO_PATH.getValue(), fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
                    if (httpResp.status / 100 == 2) {
                        List<Map> list = (List<Map>) ((Map) AppUtils.decodeJson(httpResp.content, Map.class)).get("data");
                        photoPath = (String) list.get(0).get("filePath");
                    } else {
                        log.error("上传附件错误, {}", httpResp.toString());
                    }
                }
            }

            WeixinmpOpenId weixinmpOpenId = weixinmpOpenIdService.findByOpenId(weixinmp.getId(), systemOpenId);
            if (weixinmpOpenId == null) {
                weixinmpOpenId = new WeixinmpOpenId();
                weixinmpOpenId.setWeixinmpId(weixinmp.getId());
                weixinmpOpenId.setOpenId(systemOpenId);
                weixinmpOpenId.setSecondOpenId(weixinOpenId);
                weixinmpOpenId.setCreateTime(new Date());
                weixinmpOpenIdService.insert(weixinmpOpenId);

                partnerMpOpenIdService.update(partner.getId(), systemOpenId, filterEmoji(wxMpUser.getNickname()), photoPath);
            }

            SessionUser sessionUser = new SessionUser();
            sessionUser.setAppId(weixinmp.getId());
            sessionUser.setOpenId(systemOpenId);
            sessionUser.setSecondOpenId(weixinOpenId);
            httpSession.setAttribute(AppConstant.SESSION_KEY_USER, sessionUser);

            if (forceSubscribe() && !isMpSubscribed(weixinmp, sessionUser.getSecondOpenId())) {
                String wxSubscribeUrl = weixinmp.getSubscribeUrl();
                response.sendRedirect(appendAppId(wxSubscribeUrl, appId));
                return;
            }

            targetUrl += targetUrl.contains("?") ? "&" : "?";
            String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), state, weixinmp.getId());
            response.sendRedirect(url = buildRedirectUrl(url, sessionUser, state));

            if(log.isDebugEnabled()) {
                log.debug("agentMpSnsApiUserInfo redirect url: {}", url);
            }
        }
    }

    public void agentFwSnsApiUserInfo(int appId, String systemOpenId, String auth_code, String state, String targetUrl, HttpSession httpSession, HttpServletResponse response) throws IOException {
        Alipayfw alipayfw = alipayfwService.find(appId);
        if (alipayfw == null) {
            responseText(response, "生活号配置不存在");
            return;
        }
        Partner partner = null;
        if (alipayfw.getPartnerId() == null || (partner = partnerService.find(alipayfw.getPartnerId())) == null) {
            responseText(response, "商户配置不存在");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("agentFwSnsApiUserInfo appId = {}, code = {}, state = {}", alipayfw.getId(), auth_code, state); //state是充电桩点的二维码
        }

        targetUrl = getAppHtmlPath(alipayfw.getId(), targetUrl);

        try {
            AlipayClient alipayClient = alipayClientHolder.getAlipayfw(alipayfw.getId());

            //3. 利用authCode获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(auth_code);
            oauthTokenRequest.setGrantType("authorization_code");
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);

            //成功获得authToken
            if (oauthTokenResponse != null && oauthTokenResponse.isSuccess()) {
                String photoPath = null, nickname = null;
                if(alipayfw.getUserinfoVersion() == 1) { //老版
                    //4. 利用authToken获取用户信息
                    AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
                    AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(userinfoShareRequest, oauthTokenResponse.getAccessToken());
                    if(log.isDebugEnabled()) {
                        if(userinfoShareResponse != null) {
                            log.debug("AlipayUserUserinfoShareResponse: {}", ReflectionToStringBuilder.toString(userinfoShareResponse, ToStringStyle.MULTI_LINE_STYLE));
                        }
                    }

                    if (userinfoShareResponse != null && userinfoShareResponse.isSuccess()) {
                        photoPath = userinfoShareResponse.getAvatar();
                        nickname = userinfoShareResponse.getNickName();
                    }

                } else if(alipayfw.getUserinfoVersion() == 2) { //新版
                    AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
                    AlipayUserInfoShareResponse alipayUserInfoShareResponse = alipayClient.execute(request,oauthTokenResponse.getAccessToken());
                    if(log.isDebugEnabled()) {
                        if(alipayUserInfoShareResponse != null) {
                            log.debug("AlipayUserInfoShareResponse: {}", ReflectionToStringBuilder.toString(alipayUserInfoShareResponse, ToStringStyle.MULTI_LINE_STYLE));
                        }
                    }

                    if(alipayUserInfoShareResponse.isSuccess()) {
                        photoPath = alipayUserInfoShareResponse.getAvatar();
                        nickname = alipayUserInfoShareResponse.getNickName();
                    }
                }

                if (StringUtils.isNotEmpty(photoPath)) {
                    File tempFile = new File(appConfig.tempDir, IdUtils.uuid() + ".jpg");
                    DownloadFileUtils.download(photoPath, tempFile);
                    if (tempFile.exists()) {
                        Map<String, File> fileMap = new HashMap<String, File>();
                        fileMap.put(tempFile.getName(), tempFile);

                        HttpClientUtils.HttpResp httpResp = HttpClientUtils.uploadFile(appConfig.staticUrl + "/security/upload/attachment.htm?type=" + ConstEnum.AttachmentType.CUSTOMER_PHOTO_PATH.getValue(), fileMap, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap());
                        if (httpResp.status / 100 == 2) {
                            List<Map> list = (List<Map>) ((Map) AppUtils.decodeJson(httpResp.content, Map.class)).get("data");
                            photoPath = (String) list.get(0).get("filePath");
                        } else {
                            log.error("上传附件错误, {}", httpResp.toString());
                        }
                    }
                }

                AlipayfwOpenId alipayfwOpenId = alipayfwOpenIdService.findByOpenId(alipayfw.getId(), systemOpenId);
                if (alipayfwOpenId == null) {
                    alipayfwOpenId = new AlipayfwOpenId();
                    alipayfwOpenId.setAlipayfwId(alipayfw.getId());
                    alipayfwOpenId.setOpenId(systemOpenId);
                    alipayfwOpenId.setSecondOpenId(oauthTokenResponse.getUserId());
                    alipayfwOpenId.setCreateTime(new Date());
                    alipayfwOpenIdService.insert(alipayfwOpenId);

                    partnerFwOpenIdService.update(partner.getId(), systemOpenId, filterEmoji(nickname), photoPath);
                }

                SessionUser sessionUser = new SessionUser();
                sessionUser.setAppId(alipayfw.getId());
                sessionUser.setOpenId(systemOpenId);
                sessionUser.setSecondOpenId(oauthTokenResponse.getUserId());
                httpSession.setAttribute(AppConstant.SESSION_KEY_USER, sessionUser);

                if (forceSubscribe() && !isFwSubscribed(alipayfw, sessionUser.getSecondOpenId())) {
                    String wxSubscribeUrl = alipayfw.getSubscribeUrl();
                    response.sendRedirect(appendAppId(wxSubscribeUrl, appId));
                    return;
                }

                targetUrl += targetUrl.contains("?") ? "&" : "?";
                String url = String.format("%s%sopenId=%s&%s=%s&appId=%d", appConfig.contextPath, targetUrl, sessionUser.getUnionOpenId(), getQrcodeName(), state, alipayfw.getId());
                url = buildRedirectUrl(url, sessionUser, state);
                response.sendRedirect(url);


            } else {
                log.error("agentFwSnsApiUserInfo getOpenId fail, {}", oauthTokenResponse.getBody());
            }

        } catch (AlipayApiException alipayApiException) {
            log.error(alipayApiException);
        }
    }

    protected String buildRedirectUrl(String url, SessionUser sessionUser, String state) {
        return url;
    }

    protected boolean forceSubscribe() {
        return true;
    }

}
