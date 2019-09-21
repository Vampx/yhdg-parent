package cn.com.yusong.yhdg.staticserver.web.listener;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import org.apache.zookeeper.CreateMode;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        SpringContextHolder.assertContextInjected();
        AppConfig appConfig = SpringContextHolder.getBean(AppConfig.class);
        appConfig.init(event.getServletContext());

        try {
            registerNode(appConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    void registerNode(AppConfig appConfig) throws Exception {
        ZookeeperEndpoint endpoint = new ZookeeperEndpoint(appConfig.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        appConfig.zkClient = endpoint;

        endpoint.create(ConstEnum.Node.NODE_CABINET_SERVER.getValue(), CreateMode.PERSISTENT);

        List<String> children = endpoint.addChildrenChangeListener(appConfig.cabinetServerClientManager, ConstEnum.Node.NODE_CABINET_SERVER.getValue());
        appConfig.cabinetServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_CABINET_SERVER.getValue()));
    }
}
