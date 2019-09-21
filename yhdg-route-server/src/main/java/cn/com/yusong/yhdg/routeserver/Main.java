package cn.com.yusong.yhdg.routeserver;

import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.routeserver.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener {

    static final Logger log = LogManager.getLogger(Main.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String appPath = event.getServletContext().getRealPath("");
        System.setProperty(AppConfig.SYSTEM_PROPERTY_SERVER_DIR, appPath);

        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
        try {
            config.startup();
        } catch (Exception e) {
            log.error("启动失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        AppConfig config = SpringContextHolder.getBean(AppConfig.class);
        config.close();
    }
}
