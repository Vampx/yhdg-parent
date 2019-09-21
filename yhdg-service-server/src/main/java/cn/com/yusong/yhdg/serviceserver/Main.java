package cn.com.yusong.yhdg.serviceserver;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class Main implements ServletContextListener {

    static final Logger log = LogManager.getLogger(Main.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
        try {
            config.startup(event.getServletContext());
        } catch (Exception e) {
            log.error("启动失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
