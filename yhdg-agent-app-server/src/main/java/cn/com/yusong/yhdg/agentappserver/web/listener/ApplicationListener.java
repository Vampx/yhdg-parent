package cn.com.yusong.yhdg.agentappserver.web.listener;

import cn.com.yusong.yhdg.agentappserver.service.SystemListener;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SystemListener systemListener = SpringContextHolder.getBean(SystemListener.class);
        systemListener.start(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SystemListener systemListener = SpringContextHolder.getBean(SystemListener.class);
        systemListener.stop(servletContextEvent.getServletContext());
    }
}
