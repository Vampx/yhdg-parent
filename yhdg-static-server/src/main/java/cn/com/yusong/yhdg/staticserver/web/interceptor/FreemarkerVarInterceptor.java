package cn.com.yusong.yhdg.staticserver.web.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FreemarkerVarInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		if(modelAndView != null) {
			modelAndView.addObject("controller", handlerMethod.getBean());
			modelAndView.addObject("contextPath", request.getContextPath());
		}
	}
}
