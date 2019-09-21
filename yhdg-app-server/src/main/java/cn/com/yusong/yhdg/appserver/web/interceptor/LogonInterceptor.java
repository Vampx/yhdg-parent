package cn.com.yusong.yhdg.appserver.web.interceptor;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.PushMetaDataService;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.NotVerifyAccountOtherPhoneLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.PushMessage;
import cn.com.yusong.yhdg.common.domain.basic.PushMetaData;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LogonInterceptor extends HandlerInterceptorAdapter {

    static final Logger log = LogManager.getLogger(LogonInterceptor.class);

    static final String HEADER_AUTHORIZATION = "Authorization";
    static final String RESPONSE_TEXT = "{\"code\": %d, \"message\": \"%s\", \"data\": null}";

    @Autowired
    TokenCache tokenCache;
    @Autowired
    CustomerService customerService;
    @Autowired
    PushMetaDataService pushMetaDataService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NotLogin notLogin = handlerMethod.getMethod().getAnnotation(NotLogin.class);

        if (log.isDebugEnabled()) {
            log.debug("URL = {}", request.getRequestURI());
            log.debug("header Authorization = {}", request.getHeader(HEADER_AUTHORIZATION));
        }

        TokenCache.tokenCacheHolder.set(null);

        if (notLogin == null) {
            String token = getToken(request);
            TokenCache.Data data = null;
            if (token == null || (data = tokenCache.get(token)) == null) {
                if (StringUtils.isNotEmpty(token)) {
                    log.debug("token {} 过期", token);
                }

                write(RespCode.CODE_3, response);
                return false;
            } else if (data != null) {
                if (data.isCustomer) {
                    NotVerifyAccountOtherPhoneLogin notVerifyAccountOtherPhoneLogin = handlerMethod.getMethod().getAnnotation(NotVerifyAccountOtherPhoneLogin.class);
                    if (notVerifyAccountOtherPhoneLogin == null) {
                        String loginToken = customerService.findLoginToken(data.customerId);
                        if (loginToken == null) {
                            customerService.updateLoginToken(data.customerId, data.token, null, null);
                        } else if (StringUtils.isNotEmpty(data.token) && data.token.equals(loginToken)) {
                            if (System.currentTimeMillis() - data.touchTime >= 3600 * 1000L) {
                                tokenCache.touch(data, MemCachedConfig.CACHE_THREE_DAY);
                            }

                        } else {
                            TokenCache.Data loginTokenData = tokenCache.get(loginToken);
                            if (loginTokenData != null) {
                                if (!loginToken.equals(data.token)) { //pushToken不相同说明是不同的设备
                                    log.debug("current : {}, {}", data.pushType, data.pushToken);
                                    log.debug("last login: {}, {}", loginTokenData.pushType, loginTokenData.pushToken);


                                    if (!data.pushTokenEquals(loginTokenData)) {
                                        log.debug("pushToken !");

                                        if (data.pushType != null && StringUtils.isNotEmpty(data.pushToken)) {
/*                                            PushMetaData metaData = new PushMetaData();
                                            metaData.setSourceType(PushMessage.SourceType.ACCOUNT_LOGIN_AT_ANOTHER_PHONE.getValue());
                                            metaData.setSourceId(data.customerId + ":" + data.pushType + ":" + data.pushToken);
                                            metaData.setCreateTime(new Date());
                                            pushMetaDataService.insert(metaData);*/
                                        }

                                        write(RespCode.CODE_7, response);
                                        return false;
                                    }

                                }
                            }
                        }
                    }
                }

                TokenCache.tokenCacheHolder.set(data);
            }
        }

        return true;
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (authorization != null) {
            authorization = authorization.replace("Bearer ", "");
        }
        return authorization;
    }

    private void write(RespCode code, HttpServletResponse response) throws IOException {

        String responseText = String.format(RESPONSE_TEXT, code.getValue(), code.getName());
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        response.getOutputStream().write(responseText.getBytes(Constant.CHARSET_UTF_8));
        if (log.isDebugEnabled()) {
            log.debug("response: {}", responseText);
        }
    }

    private void write(int code, String message, HttpServletResponse response) throws IOException {
        String responseText = String.format(RESPONSE_TEXT, code, message);

        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        response.getOutputStream().write(responseText.getBytes(Constant.CHARSET_UTF_8));

        if (log.isDebugEnabled()) {
            log.debug("response: {}", responseText);
        }
    }
}
