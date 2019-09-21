package cn.com.yusong.yhdg.batteryserver.web.listener;


import cn.com.yusong.yhdg.batteryserver.service.SystemListener;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        SystemListener systemListener = SpringContextHolder.getBean(SystemListener.class);
        systemListener.start(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        SystemListener systemListener = SpringContextHolder.getBean(SystemListener.class);
        systemListener.stop(event.getServletContext());
    }
}
