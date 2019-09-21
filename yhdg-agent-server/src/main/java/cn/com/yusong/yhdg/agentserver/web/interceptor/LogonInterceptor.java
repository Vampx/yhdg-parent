package cn.com.yusong.yhdg.agentserver.web.interceptor;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogonInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		NotLogin notLogin = handlerMethod.getMethod().getAnnotation(NotLogin.class);
        ViewModel viewModel = handlerMethod.getMethod().getAnnotation(ViewModel.class);
        SecurityControl securityControl = handlerMethod.getMethod().getAnnotation(SecurityControl.class);

        if(notLogin == null) {
            notLogin = handlerMethod.getBean().getClass().getAnnotation(NotLogin.class);
        }


        String contextPath = request.getContextPath();
		SessionUser sessionUser = null;
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }

        if(request.getRequestURL().indexOf("security/charger_agent")>-1){//运营商录入系统
            if (notLogin == null && sessionUser == null) { // 需要登錄
                if (viewModel == null) {
                    response.sendRedirect(String.format("%s/security/charger_agent/main/login.htm", contextPath));
                } else if (viewModel.value() == ViewModel.JSON) {
                    response.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getOutputStream(), ExtResult.timeoutResult());
                }
                return false;
            }
        }

        if (notLogin == null && sessionUser == null) { // 需要登錄

            if (viewModel == null) {
                response.sendRedirect(String.format("%s/security/main/login.htm", contextPath));

            } else if (viewModel.value() == ViewModel.JSON) {
                response.setContentType("application/json");

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getOutputStream(), ExtResult.timeoutResult());

            } else if (viewModel.value() == ViewModel.INNER_PAGE) {
                response.sendRedirect(String.format("%s/security/main/jump.htm?step=login", contextPath));
            }
            return false;

        } else {
            AppConfig appConfig = SpringContextHolder.getBean(AppConfig.class);
            if(securityControl != null && securityControl.limits() != null && securityControl.limits().length > 0) {
                String [] operCode = securityControl.limits();
                if(!sessionUser.hasAnyOper(operCode)) {
                    response.sendRedirect(String.format("%s/security/main/login.htm", contextPath));
                    return false;
                }
            }
            return true;
        }
	}
}
