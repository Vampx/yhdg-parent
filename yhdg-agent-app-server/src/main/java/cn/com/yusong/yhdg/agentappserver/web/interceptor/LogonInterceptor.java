package cn.com.yusong.yhdg.agentappserver.web.interceptor;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogonInterceptor extends HandlerInterceptorAdapter {

    static final Logger log = LogManager.getLogger(LogonInterceptor.class);

    static final String HEADER_AUTHORIZATION = "Authorization";
    static final String RESPONSE_TEXT = "{\"code\": %d, \"message\": \"%s\", \"data\": null}";

    @Autowired
    TokenCache tokenCache;

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
