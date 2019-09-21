package cn.com.yusong.yhdg.agentserver.web.listener;

import cn.com.yusong.yhdg.agentserver.utils.InstallUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Properties;

public class InstallListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        initSql(evt.getServletContext());
        upgradeSql(evt.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent evt) {

    }

    private void initSql(ServletContext servletContext) {
        try {
            File file = new File(servletContext.getRealPath("/WEB-INF/db.sql"));
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/app.properties"));
            InstallUtils.execSQL(file, true,
                    properties.getProperty("master.driver"),
                    properties.getProperty("master.url"),
                    properties.getProperty("master.user"),
                    properties.getProperty("master.password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            File file = new File(servletContext.getRealPath("/WEB-INF/history.sql"));
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/app.properties"));
            InstallUtils.execSQL(file, true,
                    properties.getProperty("history.driver"),
                    properties.getProperty("history.url"),
                    properties.getProperty("history.user"),
                    properties.getProperty("history.password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void upgradeSql(ServletContext servletContext) {
        try {
            File file = new File(servletContext.getRealPath("/WEB-INF/master_upgrade.sql"));
            if(file.exists()) {
                Properties properties = new Properties();
                properties.load(getClass().getResourceAsStream("/app.properties"));
                InstallUtils.execSQL(file, false,
                        properties.getProperty("master.driver"),
                        properties.getProperty("master.url"),
                        properties.getProperty("master.user"),
                        properties.getProperty("master.password"));
            }

            file = new File(servletContext.getRealPath("/WEB-INF/history_upgrade.sql"));
            if(file.exists()) {
                Properties properties = new Properties();
                properties.load(getClass().getResourceAsStream("/app.properties"));
                InstallUtils.execSQL(file, false,
                        properties.getProperty("history.driver"),
                        properties.getProperty("history.url"),
                        properties.getProperty("history.user"),
                        properties.getProperty("history.password"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
