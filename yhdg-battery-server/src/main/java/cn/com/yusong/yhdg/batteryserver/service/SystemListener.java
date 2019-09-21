package cn.com.yusong.yhdg.batteryserver.service;


import cn.com.yusong.yhdg.batteryserver.comm.server.MainServer;
import cn.com.yusong.yhdg.batteryserver.config.AppConfig;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.spring.SpringContextHolder;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import cn.com.yusong.yhdg.common.tool.zk.ZookeeperEndpoint;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class SystemListener {

    static Logger log = LogManager.getLogger(SystemListener.class);

    @Autowired
    AppConfig appConfig;


    public void start(ServletContext servletContext) {
        SpringContextHolder.assertContextInjected();

        try {
            appConfig.init(servletContext);
            deleteTempFile();
            registerNode();
            startMainService();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("系统初始化错误", e);
            throw new RuntimeException(e);
        }
    }

    public void stop(ServletContext ServletContext) {
        if(appConfig.zkClient != null) {
            appConfig.zkClient.close();
        }
        if(appConfig.mainServer != null) {
            appConfig.mainServer.close();
            appConfig.mainServer = null;
        }
        if(appConfig.executorServices != null) {
            for(ExecutorService e : appConfig.executorServices) {
                e.shutdown();
            }
            appConfig.executorServices = null;
        }
        if(appConfig.sessionManager != null) {
            appConfig.sessionManager.close();
        }
    }

    void deleteTempFile() throws IOException {
        AppConfig appConfig = SpringContextHolder.getBean(AppConfig.class);
        if(appConfig.tempDir.exists()) {
            try {
                FileUtils.cleanDirectory(appConfig.tempDir);
            } catch (Exception e) {
            }
        } else {
            appConfig.tempDir.mkdirs();
        }
    }

    void registerNode() throws Exception {
        ZookeeperEndpoint endpoint = new ZookeeperEndpoint(appConfig.getZkUrl(), ConstEnum.Node.NODE_YHDG.getValue());
        appConfig.zkClient = endpoint;

        String path = ConstEnum.Node.NODE_BATTERY_SERVER.getValue() + "/" + appConfig.getServerIp() + ":" + appConfig.getServerPort();
        if(endpoint.exists(path) != null) {
            endpoint.delete(path, false);
        }
        endpoint.create(path, CreateMode.EPHEMERAL, appConfig.getZkData());
        endpoint.addEphemeral(path, CreateMode.EPHEMERAL, appConfig.getZkData());
        endpoint.addDataChangeListener(appConfig.configListener, path);

        endpoint.create(ConstEnum.Node.NODE_SERVICE_SERVER.getValue(), CreateMode.PERSISTENT);
        List<String> children = endpoint.addChildrenChangeListener(appConfig.serviceServerClientManager, ConstEnum.Node.NODE_SERVICE_SERVER.getValue());
        appConfig.serviceServerClientManager.listen(new Listener.Param(children, ConstEnum.Node.NODE_SERVICE_SERVER.getValue()));
    }

    void startMainService() throws Exception {
        MainServer mainServer = new MainServer(appConfig);
        mainServer.startup();
    }
}
