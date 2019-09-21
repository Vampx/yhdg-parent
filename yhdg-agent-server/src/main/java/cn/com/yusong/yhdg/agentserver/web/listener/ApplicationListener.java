package cn.com.yusong.yhdg.agentserver.web.listener;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.agentserver.service.SystemListener;

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
