package cn.com.yusong.yhdg.weixinserver.web.interceptor;

import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
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

		String contextPath = request.getContextPath();
		SessionUser sessionUser = null;
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			sessionUser = (SessionUser) httpSession.getAttribute(AppConstant.SESSION_KEY_USER);
		}

		if (notLogin == null && sessionUser == null) { // 需要登录
			AppConfig config = SpringContextHolder.getBean(AppConfig.class);

			if(viewModel == null) { //大页面
				//response.sendRedirect(config.getWxJumpUrl());
			} else if(viewModel.value() == ViewModel.INNER_PAGE) {
				response.sendRedirect(contextPath + "/main/timeout.htm");

			} else if(viewModel.value() == ViewModel.JSON) {
				response.setContentType("application/json");
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.writeValue(response.getOutputStream(), ExtResult.timeoutResult());
			}

			return false;
		}

		return true;
	}
}
